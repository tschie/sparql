package io.github.tschie.sparql.query

class InvalidExpressionException(val expression: QueryExpression) : Exception(format(expression))