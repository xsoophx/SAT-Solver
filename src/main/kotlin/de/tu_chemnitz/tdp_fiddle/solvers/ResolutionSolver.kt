package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause
import de.tu_chemnitz.tdp_fiddle.Literal

object ResolutionSolver : SATSolver {

    override fun isSolvable(cnf: Set<Clause>): Boolean = resolution(cnf)

    private fun resolution(input: Set<Clause>): Boolean {
        val cnf = input.removeTautologies()
        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false

        val literals = cnf.asSequence().flatMap { clause -> clause.literals.asSequence() }.toSet()
        val (literal, clauses) = literals.firstNotNullOfOrNull { literal ->
            literal.findClausesToResolve(cnf)?.let { clauses -> literal to clauses }
        } ?: return cnf.isNotEmpty()

        val nextCnf = cnf.toMutableSet().apply {
            remove(clauses.first)
            remove(clauses.second)
            add(createResolvent(clauses.first, clauses.second, literal))
        }.toSet()

        return resolution(nextCnf)
    }

    private fun Set<Clause>.removeTautologies(): Set<Clause> =
        asSequence().mapNotNull { clause -> clause.removeTautology() }.toSet()

    private fun Clause.removeTautology(): Clause? {
        literals.forEach { literal ->
            if (literal.negated() in literals)
                return null
        }
        return this
    }

    private fun Literal.findClausesToResolve(cnf: Set<Clause>): Pair<Clause, Clause>? {
        val positiveClause = findClause(cnf) ?: return null
        val negativeClause = negated().findClause(cnf) ?: return null
        if (positiveClause != negativeClause)
            return Pair(positiveClause, negativeClause)

        return null
    }

    private fun Literal.findClause(cnf: Set<Clause>): Clause? = cnf.find { clause -> this in clause.literals }

    private fun createResolvent(c: Clause, d: Clause, literal: Literal): Clause {
        return if (literal in c.literals)
            Clause(c.literals - literal + d.literals - literal.negated())
        else
            Clause(c.literals - literal.negated() + d.literals - literal)
    }
}