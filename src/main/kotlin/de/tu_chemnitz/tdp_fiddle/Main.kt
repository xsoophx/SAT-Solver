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

    fun readInput(input: String, clausesOfTwo: Boolean = false): List<Clause> =
        input.trim().splitToSequence("*").mapIndexed { index, value ->
            try {
                if (clausesOfTwo) createClausesOfTwo(value) else createClause(value)
            } catch (e: InvalidClauseException) {
                throw InvalidFormulaException(indexClause = index, indexLiteral = e.index, e)
            }
        }.toList()

    private fun createClausesOfTwo(clause: String): Clause = createClause(clause, 2)

    private fun createClause(clause: String, literalsPerClause: Int? = null): Clause {
        return when (literalsPerClause != null) {
            true -> Clause(clause.getContent().take(literalsPerClause).createLiterals())
            else -> Clause(clause.getContent().createLiterals())
        }
    }

    private fun String.getContent() =
        trim().filterNot(Char::isWhitespace).removeSurrounding("(", ")").splitToSequence(OR)

    private fun Sequence<String>.createLiterals(): Set<Literal> {
        return mapIndexed { index, value ->
            try {
                createLiteral(value)
            } catch (e: IllegalArgumentException) {
                throw InvalidClauseException(index, e)
            }
        }.toSet()
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
