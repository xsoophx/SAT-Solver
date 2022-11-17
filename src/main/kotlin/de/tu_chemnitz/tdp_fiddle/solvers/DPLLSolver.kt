package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause
import de.tu_chemnitz.tdp_fiddle.Literal

object DPLLSolver : SATSolver {

    override fun isSolvable(cnf: Set<Clause>): Boolean = dpll(cnf)

    private fun dpll(cnf: Set<Clause>): Boolean {
        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false
        if (cnf.containsContradiction()) return false

        val unitClause = cnf.firstOrNull { clause -> clause.size == 1 }

        if (unitClause != null) {
            val newCnf = cnf.setByLiteral(unitClause.first())
            return dpll(newCnf)
        } else {
            val literal = cnf.first().literals.firstOrNull()?.value ?: return false
            val positive = cnf.setByLiteral(Literal(literal, true))
            val negative = cnf.setByLiteral(Literal(literal, false))

            return dpll(positive) || dpll(negative)
        }
    }
}