package de.tu_chemnitz.tdp_fiddle

interface SATSolver {
    fun isSolvable(cnf: Set<Clause>): Boolean
}