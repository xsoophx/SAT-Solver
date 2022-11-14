package de.tu_chemnitz.tdp_fiddle.solvers

import de.tu_chemnitz.tdp_fiddle.Clause

interface SATSolver {
    fun isSolvable(cnf: Set<Clause>): Boolean
}