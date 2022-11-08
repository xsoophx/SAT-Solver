package de.tu_chemnitz.tdp_fiddle

object SATSolver {

    fun isSolvable(cnf: Set<Clause>): Boolean {
        val uniqueLiterals = cnf.getUniqueLiterals()

        return dp(cnf, uniqueLiterals)
    }

    private fun dp(cnf: Set<Clause>, uniqueLiterals: Set<String>): Boolean {
        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false
        if (!cnf.containsContradiction()) return false

        val literal = uniqueLiterals.firstOrNull() ?: return false
        val remainingLiterals = uniqueLiterals - literal

        val positive = cnf.simplify(Literal(literal, true))
        val negative = cnf.simplify(Literal(literal, false))


        return dp(positive, remainingLiterals) || dp(negative, remainingLiterals)
    }

    private fun Set<Clause>.containsContradiction(): Boolean {
        val oneLiterals = mutableSetOf<Literal>()

        asSequence()
            .filter { it.literals.size == 1 }
            .forEach { clause ->
                val literal = clause.literals.first()
                val negated = Literal(literal.value, !literal.isPositive)

                if (negated in oneLiterals) return false

                oneLiterals.add(literal)
            }
        return true
    }

    private fun Set<Clause>.getUniqueLiterals(): Set<String> =
        asSequence().flatMap { clause -> clause.literals.asSequence() }.map { it.value }.toSet()

    private fun Clause.simplify(literal: Literal): Clause? {
        if (literal in literals)
            return null

        return Clause(literals - literal.copy(isPositive = !literal.isPositive))
    }

    private fun Set<Clause>.simplify(literal: Literal): Set<Clause> =
        asSequence().mapNotNull { clause -> clause.simplify(literal) }.toSet()

}