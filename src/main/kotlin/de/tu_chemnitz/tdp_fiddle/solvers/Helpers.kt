package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause
import de.tu_chemnitz.tdp_fiddle.Literal

internal fun Set<Clause>.containsContradiction(): Boolean {
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

internal fun Set<Clause>.setByLiteral(literal: Literal): Set<Clause> =
    asSequence().mapNotNull { clause -> clause.setByLiteral(literal) }.toSet()

private fun Clause.setByLiteral(literal: Literal): Clause? {
    if (literal in literals)
        return null

    return Clause(literals - literal.negated())
}
