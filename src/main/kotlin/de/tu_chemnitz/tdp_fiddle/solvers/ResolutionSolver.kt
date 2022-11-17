package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object ResolutionSolver : SATSolver {

    private val resolvents: MutableSet<Clause> = mutableSetOf()

    override fun isSolvable(cnf: Set<Clause>): Boolean = prepareResolution(cnf)

    private fun prepareResolution(cnf: Set<Clause>): Boolean {
        resolvents.clear()
        if (cnf.isEmpty()) return true
        if (cnf.any { it.literals.isEmpty() }) return false

        return resolution(cnf.removeTautologies())
    }

    private fun resolution(cnf: Set<Clause>): Boolean {
        cnf.combineAllElements(cnf, ::resolve)
        return resolvents.all { it.isNotEmpty() }
    }

    private inline fun Set<Clause>.combineAllElements(
        cnf: Set<Clause>,
        block: (Set<Clause>, Clause, Clause) -> Boolean
    ) {
        forEach { outerClause ->
            forEach { innerClause ->
                if (outerClause != innerClause && !block(cnf, outerClause, innerClause)) return
            }
        }
    }

    private fun resolve(cnf: Set<Clause>, outerClause: Clause, innerClause: Clause): Boolean {
        val currentResolvents = findAllResolvents(outerClause, innerClause)

        if (currentResolvents.isNotEmpty()) {
            currentResolvents.forEach { resolvent ->
                resolvents.add(resolvent)
                if (resolvent.isEmpty()) return false

                val nextCnf = cnf.toMutableSet().apply {
                    remove(outerClause)
                    remove(innerClause)
                    add(resolvent)
                }.toSet()

                logger.info { "$nextCnf" }
                return resolution(nextCnf)
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