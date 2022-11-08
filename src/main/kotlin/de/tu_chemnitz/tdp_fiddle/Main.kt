package de.tu_chemnitz.tdp_fiddle

object Main {
    private const val OR = '+'
    private const val NEGATOR = '-'

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            readInput(readln())
        } catch (e: InvalidFormulaException) {
            println("Wrong character in clause ${e.indexClause} at literal ${e.indexLiteral}.")
        }
    }

    //TODO: add fancy logic
    /*
    fun List<Clause>.getAdjacencyList(): Map<Literal, Literal> {
        return asSequence().flatMap {
            it.literals.asSequence().map { it to it }
        }.toMap()
    }*/

    fun readInput(input: String): List<Clause> =
        input.trim().splitToSequence("*").mapIndexed { index, value ->
            try {
                createClause(value)
            } catch (e: InvalidClauseException) {
                throw InvalidFormulaException(indexClause = index, indexLiteral = e.index, e)
            }
        }.toList()

    private fun createClause(clause: String): Clause {
        val literals = clause
            .trim()
            .filterNot(Char::isWhitespace)
            .removeSurrounding("(", ")")
            .splitToSequence(OR)
            .mapIndexed { index, value ->
                try {
                    createLiteral(value)
                } catch (e: IllegalArgumentException) {
                    throw InvalidClauseException(index, e)
                }
            }.toList()

        return Clause(literals)
    }

    private fun createLiteral(value: String): Literal {
        val firstChar = value.first()

        return when {
            firstChar == NEGATOR -> Literal(value.drop(1), false)
            firstChar.isLetter() -> Literal(value, true)
            else -> throw IllegalArgumentException("Literal \"$value\" did not start with a letter.")
        }
    }
}

data class Literal(
    val value: String,
    val isPositive: Boolean
)

data class Clause(
    val literals: List<Literal>
)