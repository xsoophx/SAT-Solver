package de.tu_chemnitz.tdp_fiddle

object DPSolver : SATSolver {

    override fun isSolvable(cnf: Set<Clause>): Boolean = dp(cnf)

    private fun dp(cnf: Set<Clause>): Boolean {
        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false
        if (cnf.containsContradiction()) return false

        val literal = cnf.first().literals.firstOrNull()?.value ?: return false

        val positive = cnf.setByLiteral(Literal(literal, true))
        val negative = cnf.setByLiteral(Literal(literal, false))


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

    private fun Clause.setByLiteral(literal: Literal): Clause? {
        if (literal in literals)
            return null

        return Clause(literals - literal.negated())
    }

    private fun Set<Clause>.setByLiteral(literal: Literal): Set<Clause> =
        asSequence().mapNotNull { clause -> clause.setByLiteral(literal) }.toSet()

}
