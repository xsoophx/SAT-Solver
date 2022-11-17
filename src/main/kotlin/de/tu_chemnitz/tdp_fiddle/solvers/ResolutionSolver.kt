package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause

object ResolutionSolver : SATSolver {

    private val resolvents: MutableSet<Clause> = mutableSetOf()

    override fun isSolvable(cnf: Set<Clause>): Boolean = prepareResolution(cnf)

    private fun prepareResolution(cnf: Set<Clause>): Boolean {
        resolvents.clear()
        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false

        resolution(cnf.removeTautologies())
        return resolvents.all { it.literals.isNotEmpty() }
    }

    private fun resolution(cnf: Set<Clause>) {
        cnf.combineAllElements(cnf, ::resolve)
    }

    private inline fun Set<Clause>.combineAllElements(cnf: Set<Clause>, block: (Set<Clause>, Clause, Clause) -> Unit) {
        asSequence().forEach { outerClause ->
            asSequence().forEach { innerClause ->
                block(cnf, outerClause, innerClause)
            }
        }
    }

    private fun resolve(cnf: Set<Clause>, outerClause: Clause, innerClause: Clause) {
        val currentResolvents = findAllResolvents(outerClause, innerClause)

        if (outerClause != innerClause && currentResolvents.isNotEmpty()) {
            currentResolvents.asSequence().forEach { resolvent ->
                resolvents.add(resolvent)
                if (resolvent.isEmpty()) return

                val nextCnf = cnf.toMutableSet().apply {
                    remove(outerClause)
                    remove(innerClause)
                    add(resolvent)
                }.toSet()

                resolution(nextCnf)
            }
        }
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