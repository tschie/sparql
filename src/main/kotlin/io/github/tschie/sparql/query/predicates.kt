package io.github.tschie.sparql.query

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl
import org.hibernate.query.criteria.internal.ParameterRegistry
import org.hibernate.query.criteria.internal.Renderable
import org.hibernate.query.criteria.internal.compile.RenderingContext
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate

fun CriteriaBuilder.lessThanUntyped(x: Expression<*>, y: Expression<*>) : Predicate {
    val criteriaBuilderImpl = this as CriteriaBuilderImpl
    return object: AbstractSimplePredicate(criteriaBuilderImpl) {
        override fun registerParameters(registry: ParameterRegistry?) {
            // Nothing to register
        }

        override fun render(isNegated: Boolean, renderingContext: RenderingContext?): String {
            return " ${(x as Renderable).render(renderingContext)} < ${(y as Renderable).render(renderingContext)}"
        }
    }
}

fun CriteriaBuilder.lessThanOrEqualToUntyped(x: Expression<*>, y: Expression<*>) : Predicate {
    val criteriaBuilderImpl = this as CriteriaBuilderImpl
    return object: AbstractSimplePredicate(criteriaBuilderImpl) {
        override fun registerParameters(registry: ParameterRegistry?) {
            // Nothing to register
        }

        override fun render(isNegated: Boolean, renderingContext: RenderingContext?): String {
            return " ${(x as Renderable).render(renderingContext)} <= ${(y as Renderable).render(renderingContext)}"
        }
    }
}

fun CriteriaBuilder.greaterThanUntyped(x: Expression<*>, y: Expression<*>) : Predicate {
    val criteriaBuilderImpl = this as CriteriaBuilderImpl
    return object: AbstractSimplePredicate(criteriaBuilderImpl) {
        override fun registerParameters(registry: ParameterRegistry?) {
            // Nothing to register
        }

        override fun render(isNegated: Boolean, renderingContext: RenderingContext?): String {
            return " ${(x as Renderable).render(renderingContext)} > ${(y as Renderable).render(renderingContext)}"
        }
    }
}

fun CriteriaBuilder.greaterThanOrEqualToUntyped(x: Expression<*>, y: Expression<*>) : Predicate {
    val criteriaBuilderImpl = this as CriteriaBuilderImpl
    return object: AbstractSimplePredicate(criteriaBuilderImpl) {
        override fun registerParameters(registry: ParameterRegistry?) {
            // Nothing to register
        }

        override fun render(isNegated: Boolean, renderingContext: RenderingContext?): String {
            return " ${(x as Renderable).render(renderingContext)} >= ${(y as Renderable).render(renderingContext)}"
        }
    }
}