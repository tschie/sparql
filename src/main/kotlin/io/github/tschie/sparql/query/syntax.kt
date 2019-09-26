package io.github.tschie.sparql.query

import java.text.SimpleDateFormat
import java.util.*

/**
 * Parses a query string into an expression.
 */
fun parse(query: String) : QueryExpression {
    val q = query.trim() // ignore excess white space
    return when {
        q.startsWith("(") && q.endsWith(")") -> GroupExpression(parse(q.removeSurrounding("(", ")")))
        q.startsWith("distance(") && q.endsWith(")") -> DistanceFunctionExpression(parse(q.removeSurrounding("distance(", ")")))
        q.matches(Regex("""POINT\(.*?\)$""")) -> WKTExpression(q)
        q.contains("OR") -> {
            var s = q
            val subExpressions = mutableListOf<QueryExpression>()
            while(s.split("OR".toRegex(), 2).size > 1) {
                subExpressions.add(parse(s.split("OR".toRegex(), 2)[0]))
                s = s.split("OR".toRegex(), 2)[1]
            }
            subExpressions.add(parse(s))
            OrListExpression(subExpressions)
        }
        q.contains("AND") -> {
            var s = q
            val subExpressions = mutableListOf<QueryExpression>()
            while(s.split("AND".toRegex(), 2).size > 1) {
                subExpressions.add(parse(s.split("AND".toRegex(), 2)[0]))
                s = s.split("AND".toRegex(), 2)[1]
            }
            subExpressions.add(parse(s))
            AndListExpression(subExpressions)
        }
        q.matches(Regex("""(.*?)==(.*?)""")) -> EqualsExpression(parse(q.split("==")[0]), parse(q.split("==")[1]))
        q.matches(Regex("""(.*?)!=(.*?)""")) -> NotEqualsExpression(parse(q.split("!=")[0]), parse(q.split("!=")[1]))
        q.matches(Regex("""(.*?)<=(.*?)""")) -> LessThanOrEqualExpression(parse(q.split("<=")[0]), parse(q.split("<=")[1]))
        q.matches(Regex("""(.*?)>=(.*?)""")) -> GreaterThanOrEqualExpression(parse(q.split(">=")[0]), parse(q.split(">=")[1]))
        q.matches(Regex("""(.*?)<(.*?)""")) -> LessThanExpression(parse(q.split("<")[0]), parse(q.split("<")[1]))
        q.matches(Regex("""(.*?)>(.*?)""")) -> GreaterThanExpression(parse(q.split(">")[0]), parse(q.split(">")[1]))
        q.contains(",") -> {
            var s = q
            val subExpressions = mutableListOf<QueryExpression>()
            while(s.split(",".toRegex(), 2).size > 1) {
                subExpressions.add(parse(s.split(",".toRegex(), 2)[0]))
                s = s.split(",".toRegex(), 2)[1]
            }
            subExpressions.add(parse(s))
            ListExpression(subExpressions)
        }
        q == "true" || q == "false" -> BooleanExpression(q == "true")
        q.matches("""[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]""".toRegex()) -> DateExpression(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(q))
        q.matches("""^[0-9A-F]{8}-[0-9A-F]{4}-[4][0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$""".toRegex(RegexOption.IGNORE_CASE)) -> UUIDExpression(UUID.fromString(q))
        Regex("""^[-+]?[0-9]\d*(\.\d+)?$""").matchEntire(q) != null -> NumericExpression(q.toDouble())
        q.startsWith("$") -> ParameterExpression(q.substring(1))
        q.matches(Regex("""(^".*?")""")) -> StringExpression(q.substring(1, q.length - 1))
        else -> InvalidExpression(q)
    }
}

/**
 * Formats an expression into a query string.
 */
fun format(expression: QueryExpression) : String = with(expression) {
    when(this) {
        is GroupExpression -> "(${format(innerExpression)})"
        is ListExpression -> expressions.joinToString(separator = ",") { format(it) }
        is OrListExpression -> expressions.joinToString(separator = "OR") { format(it) }
        is AndListExpression -> expressions.joinToString(separator = "AND") { format(it) }
        is FunctionExpression -> "$functionAlias(${format(argumentExpression)})"
        is PredicateExpression -> "${format(subjectExpression)}$symbol${format(objectExpression)}"
        is ParameterExpression -> "\$$parameterName"
        is StringExpression -> "\"$value\""
        is BooleanExpression -> value.toString()
        is NumericExpression -> value.toString()
        is UUIDExpression -> value.toString()
        is DateExpression -> SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value)
        is WKTExpression -> value
        is InvalidExpression -> "{$invalidString}"
    }
}
