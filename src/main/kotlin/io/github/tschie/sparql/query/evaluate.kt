package io.github.tschie.sparql.query

import io.github.tschie.sparql.entities.Location
import io.github.tschie.sparql.entities.LocationStream
import org.geolatte.geom.Geometry
import org.geolatte.geom.codec.Wkt
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.*

/**
 * Translates an expression into a specification.
 */
@Throws(exceptionClasses = [UnsupportedExpressionException::class, InvalidExpressionException::class])
fun <T> evaluate(entityType: Class<*>, expression: QueryExpression) : Specification<T> = with(expression) {
    when (this) {
        is GroupExpression -> evaluate(entityType, innerExpression)
        is OrListExpression -> {
            var spec = Specification.where<T>(evaluate(entityType, expressions[0]))
            for (i in 1 until expressions.size) {
                spec = spec.or(evaluate(entityType, expressions[i]))
            }
            spec
        }
        is AndListExpression -> {
            var spec = Specification.where<T>(evaluate(entityType, expressions[0]))
            for (i in 1 until expressions.size) {
                spec = spec.and(evaluate(entityType, expressions[i]))
            }
            spec
        }
        is PredicateExpression -> Specification { root, _, builder ->
            val subjectExpression = evaluate<T>(entityType, root, builder, subjectExpression)
            val objectExpression = evaluate<T>(entityType, root, builder, objectExpression)
            if (subjectExpression.javaType == objectExpression.javaType) {
                val type = subjectExpression.javaType
                when {
                    this is EqualsExpression && (type == Double::class.javaObjectType || type == Boolean::class.javaObjectType || type == String::class.java || type == UUID::class.javaObjectType) -> builder.equal(subjectExpression, objectExpression)
                    this is NotEqualsExpression && (type == Double::class.javaObjectType || type == Boolean::class.javaObjectType || type == String::class.java) -> builder.notEqual(subjectExpression, objectExpression)
                    this is LessThanExpression && (type == Double::class.javaObjectType) -> builder.lessThanUntyped(subjectExpression, objectExpression)
                    this is GreaterThanExpression && (type == Double::class.javaObjectType) -> builder.greaterThanUntyped(subjectExpression, objectExpression)
                    this is GreaterThanOrEqualExpression && (type == Double::class.javaObjectType || type == Date::class.javaObjectType) -> builder.greaterThanOrEqualToUntyped(subjectExpression, objectExpression)
                    this is LessThanOrEqualExpression && (type == Double::class.javaObjectType || type == Date::class.javaObjectType) -> builder.lessThanOrEqualToUntyped(subjectExpression, objectExpression)
                    else -> throw UnsupportedExpressionException(expression)
                }
            } else throw UnsupportedExpressionException(expression)
        }
        is InvalidExpression -> throw InvalidExpressionException(expression)
        else -> throw UnsupportedExpressionException(expression)
    }
}

/**
 * Translates a query expression into a specification expression.
 */
fun <T> evaluate(entityType: Class<*>, root: Root<*>, builder: CriteriaBuilder, expression: QueryExpression) : Expression<*> {
    return with (expression) {
        when (this) {
            is StringExpression -> builder.literal<String>(value)
            is BooleanExpression -> builder.literal<Boolean>(value)
            is NumericExpression -> builder.literal<Double>(value)
            is UUIDExpression -> builder.literal<UUID>(value)
            is DateExpression -> builder.literal<Date>(value)
            is WKTExpression -> builder.literal<Geometry<*>>(Wkt.fromWkt(value) as Geometry<*>)
            is OrListExpression -> throw UnsupportedExpressionException(expression)
            is AndListExpression -> throw UnsupportedExpressionException(expression)
            is GroupExpression -> evaluate<T>(entityType, root, builder, this)
            is PredicateExpression -> throw UnsupportedExpressionException(expression)
            is FunctionExpression -> {
                when (argumentExpression) {
                    is ListExpression -> builder.function(functionName, functionReturnType, *(argumentExpression as ListExpression).expressions.map { evaluate<T>(entityType, root, builder, it) }.toTypedArray())
                    is ParameterExpression -> builder.function(functionName, functionReturnType, evaluate<T>(entityType, root, builder, argumentExpression))
                    is ValueExpression -> builder.function(functionName, functionReturnType, evaluate<T>(entityType, root, builder, argumentExpression))
                    else -> throw UnsupportedExpressionException(expression)
                }
            }
            is ParameterExpression -> evaluateParameterExpression<T>(entityType, root, this)
            is InvalidExpression -> throw InvalidExpressionException(expression)
            else -> throw UnsupportedExpressionException(expression)
        }
    }
}

/**
 * Translates a ParameterExpression into a specification expression. Supports nested parameters with joins.
 */
fun <T> evaluateParameterExpression(entityType: Class<*>, from: Path<*>, parameterExpression: ParameterExpression) : Expression<*> {
    val paramParts = parameterExpression.parameterName.split("\\.".toRegex(), 2)
    val parameterName = paramParts[0]
    val parameter = entityType.getDeclaredField(parameterName)
    val subFrom = when {
        parameter.type == Double::class.javaObjectType -> from.get<Double>(parameterName)
        parameter.type == String::class.java -> from.get<String>(parameterName)
        parameter.type == Boolean::class.javaObjectType -> from.get<Boolean>(parameterName)
        parameter.type == UUID::class.java -> from.get<UUID>(parameterName)
        parameter.type == Date::class.java -> from.get<Date>(parameterName)
        parameter.type == Geometry::class.java -> from.get<Geometry<*>>(parameterName)
        parameter.type == Location::class.java && from is From<*, *> -> from.join<T, Location>(parameterName)
        parameter.type ==  LocationStream::class.java && from is From<*, *> -> from.join<T, LocationStream>(parameterName)
        else -> throw UnsupportedExpressionException(parameterExpression)
    }
    return if (paramParts.size > 1) {
        evaluateParameterExpression<T>(parameter.type, subFrom, ParameterExpression(paramParts[1]))
    } else {
        subFrom
    }
}