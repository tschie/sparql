package io.github.tschie.sparql.query

class UnsupportedExpressionException(val expression: QueryExpression) : Exception(format(expression))