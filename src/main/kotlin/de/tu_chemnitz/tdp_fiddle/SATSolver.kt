package de.tu_chemnitz.tdp_fiddle

object SATSolver {

    fun isSolvable(cnf: Set<Clause>, method: Method = Method.DP): Boolean {
        return when (method) {
            Method.DP -> dp(cnf)
            else -> resolution(cnf)
        }
    }

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

    private fun Clause.removeTautology(): Clause? {
        literals.forEach { literal ->
            if (literal.negated() in literals)
                return null
        }
        return this
    }

    private fun Set<Clause>.removeTautologies(): Set<Clause> =
        asSequence().mapNotNull { clause -> clause.removeTautology() }.toSet()

    private fun Clause.setByLiteral(literal: Literal): Clause? {
        if (literal in literals)
            return null

        return Clause(literals - literal.negated())
    }

    private fun Set<Clause>.setByLiteral(literal: Literal): Set<Clause> =
        asSequence().mapNotNull { clause -> clause.setByLiteral(literal) }.toSet()

}
