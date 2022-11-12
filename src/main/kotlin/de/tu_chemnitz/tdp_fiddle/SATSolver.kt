package de.tu_chemnitz.tdp_fiddle

object SATSolver {

    fun isSolvable(cnf: Set<Clause>): Boolean {
        return dp(cnf)
    }

    private fun dp(cnf: Set<Clause>): Boolean {
        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false
        if (cnf.containsContradiction()) return false

        val literal = cnf.first().literals.firstOrNull()?.value ?: return false

        val positive = cnf.simplify(Literal(literal, true))
        val negative = cnf.simplify(Literal(literal, false))


        return dp(positive) || dp(negative)
    }

    private fun Set<Clause>.containsContradiction(): Boolean {
        val oneLiterals = mutableSetOf<Literal>()

        asSequence()
            .filter { it.literals.size == 1 }
            .forEach { clause ->
                val literal = clause.literals.first()
                val negated = Literal(literal.value, !literal.isPositive)

                if (negated in oneLiterals) return true

                oneLiterals.add(literal)
            }
        return false
    }

    private fun Clause.simplify(literal: Literal): Clause? {
        if (literal in literals)
            return null

        return Clause(literals - literal.copy(isPositive = !literal.isPositive))
    }

    private fun Set<Clause>.simplify(literal: Literal): Set<Clause> =
        asSequence().mapNotNull { clause -> clause.simplify(literal) }.toSet()

}