package de.tu_chemnitz.tdp_fiddle

import java.security.InvalidParameterException

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

    fun readInput(input: String): List<Clause> =
        input.trim().splitToSequence("*").mapIndexed { index, value ->
            try {
                createClause(value)
            } catch (e: InvalidClauseException) {
                throw InvalidFormulaException(indexClause = index, indexLiteral = e.index)
            }
        }.toList()

    private fun createLiteral(value: String): Literal {
        val firstChar = value.first()

        return when {
            firstChar == NEGATOR -> Literal(value.drop(1), false)
            firstChar.isLetter() -> Literal(value, true)
            else -> throw InvalidParameterException()
        }
    }

    private fun createClause(clause: String): Clause {
        val literals = clause
            .trim()
            .filter { !it.isWhitespace() }
            .removeSurrounding("(", ")")
            .splitToSequence(OR)
            .mapIndexed { index, value ->
                try {
                    createLiteral(value)
                } catch (e: IllegalArgumentException) {
                    throw InvalidClauseException(index)
                }
            }.toList()

        return Clause(
            firstLiteral = literals[0],
            secondLiteral = literals[1],
            thirdLiteral = literals[2],
        )
    }
}

data class Literal(
    val value: String,
    val isPositive: Boolean
)

data class Clause(
    val firstLiteral: Literal,
    val secondLiteral: Literal,
    val thirdLiteral: Literal,
)