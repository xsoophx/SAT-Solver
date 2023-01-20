package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause

object ResolutionSolver : SATSolver {

    private val currentResolvents: MutableSet<Clause> = mutableSetOf()
    private val newResolvents: MutableSet<Clause> = mutableSetOf()

    override fun isSolvable(cnf: Set<Clause>): Boolean = prepareResolution(cnf)

    private fun prepareResolution(cnf: Set<Clause>): Boolean {
        currentResolvents.clear()
        newResolvents.clear()

        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false
        currentResolvents += cnf
        newResolvents += cnf

        return resolution(cnf.removeTautologies())
    }

    private fun resolution(cnf: Set<Clause>): Boolean {
        val satisfiable = cnf.combineAllElements(::resolve)

        if (!satisfiable)
            return false

        if (newResolvents != currentResolvents) {
            currentResolvents += newResolvents
            return resolution(currentResolvents)
        }

        return true
    }

    private inline fun Set<Clause>.combineAllElements(
        block: (Clause, Clause) -> Boolean
    ): Boolean {
        forEach { outerClause ->
            forEach { innerClause ->
                if (outerClause != innerClause && !block(outerClause, innerClause)) return false
            }
        }
        return true
    }

    private fun resolve(outerClause: Clause, innerClause: Clause): Boolean {
        val currentResolvents = findAllResolvents(outerClause, innerClause)

        if (currentResolvents.isNotEmpty()) {
            currentResolvents.forEach { resolvent ->
                if (resolvent.isEmpty()) return false

                newResolvents += resolvent
            }
        }
        return true
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

    private fun findAllResolvents(c: Clause, d: Clause): Set<Clause> {
        return c.literals.mapNotNull { literal ->
            if (literal.negated() in d.literals) {
                Clause(c.literals - literal + d.literals - literal.negated())
            } else null
        }.toSet()
    }
}