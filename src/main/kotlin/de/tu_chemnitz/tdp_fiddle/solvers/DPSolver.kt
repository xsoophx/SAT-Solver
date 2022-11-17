package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause
import de.tu_chemnitz.tdp_fiddle.Literal

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
}
