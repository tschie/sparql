package io.github.tschie.sparql.query

import java.util.*

sealed class QueryExpression

data class ListExpression(val expressions: List<QueryExpression>) : QueryExpression()
data class OrListExpression(val expressions: List<QueryExpression>) : QueryExpression()
data class AndListExpression(val expressions: List<QueryExpression>) : QueryExpression()

data class GroupExpression(val innerExpression: QueryExpression) : QueryExpression()

sealed class PredicateExpression(open val subjectExpression: QueryExpression, val symbol: String, open val objectExpression: QueryExpression) : QueryExpression()

data class EqualsExpression(override val subjectExpression: QueryExpression, override val objectExpression: QueryExpression) : PredicateExpression(subjectExpression, "==", objectExpression)
data class NotEqualsExpression(override val subjectExpression: QueryExpression, override val objectExpression: QueryExpression) : PredicateExpression(subjectExpression, "!=", objectExpression)
data class GreaterThanExpression(override val subjectExpression: QueryExpression, override val objectExpression: QueryExpression) : PredicateExpression(subjectExpression, ">", objectExpression)
data class GreaterThanOrEqualExpression(override val subjectExpression: QueryExpression, override val objectExpression: QueryExpression) : PredicateExpression(subjectExpression, ">=", objectExpression)
data class LessThanExpression(override val subjectExpression: QueryExpression, override val objectExpression: QueryExpression) : PredicateExpression(subjectExpression, "<", objectExpression)
data class LessThanOrEqualExpression(override val subjectExpression: QueryExpression, override val objectExpression: QueryExpression) : PredicateExpression(subjectExpression, "<=", objectExpression)

sealed class FunctionExpression(open val functionAlias: String, open val functionName: String, open val functionReturnType: Class<*>, open val argumentExpression: QueryExpression) : QueryExpression()
data class DistanceFunctionExpression(override val argumentExpression: QueryExpression) : FunctionExpression
("distance", "ST_3DDistance", Double::class.javaObjectType, argumentExpression)

data class ParameterExpression(val parameterName: String) : QueryExpression()

sealed class ValueExpression : QueryExpression()

data class StringExpression(val value: String) : ValueExpression()
data class BooleanExpression(val value: Boolean) : ValueExpression()
data class NumericExpression(val value: Double) : ValueExpression()
data class UUIDExpression(val value: UUID) : ValueExpression()

data class DateExpression(val value: Date) : ValueExpression()

data class WKTExpression(val value: String) : ValueExpression()

data class InvalidExpression(val invalidString: String) : QueryExpression()