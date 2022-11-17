package de.tu_chemnitz.tdp_fiddle

import de.tu_chemnitz.tdp_fiddle.solvers.DPLLSolver
import de.tu_chemnitz.tdp_fiddle.solvers.DPSolver
import de.tu_chemnitz.tdp_fiddle.solvers.ResolutionSolver
import de.tu_chemnitz.tdp_fiddle.solvers.SATSolver
import java.util.stream.Stream
import kotlin.test.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SATSolverTest {

    @ParameterizedTest
    @MethodSource("getSolvers")
    fun `empty formula is solvable`(solver: SATSolver) {
        val emptyFormula = setOf<Clause>()
        assertEquals(expected = true, actual = solver.isSolvable(emptyFormula))
    }

    @ParameterizedTest
    @MethodSource("getSolvers")
    fun `formulas with empty clauses are not solvable`(solver: SATSolver) {
        val emptyClause = setOf(Clause(emptySet()))
        assertEquals(expected = false, actual = solver.isSolvable(emptyClause))
    }

    @ParameterizedTest
    @MethodSource("getInputByCNF")
    fun `gets formulas correctly with dp`(cnf: String, expected: Boolean) {
        val actual = DPSolver.isSolvable(Main.readInput(cnf).toSet())
        assertEquals(expected = expected, actual = actual)
    }

    @ParameterizedTest
    @MethodSource("getInputByCNF")
    fun `gets formulas correctly with resolution`(cnf: String, expected: Boolean) {
        val actual = ResolutionSolver.isSolvable(Main.readInput(cnf).toSet())
        assertEquals(expected = expected, actual = actual)
    }

    companion object {
        @JvmStatic
        private fun getSolvers() = Stream.of(
            Arguments.of(DPSolver),
            Arguments.of(DPLLSolver),
            Arguments.of(ResolutionSolver)
        )

        @JvmStatic
        private fun getInputByCNF() = Stream.of(
            Arguments.of(
                "(A + B) * (-A + B) * (A + -B) * (-A + -B)", false
            ),
            Arguments.of(
                "(a) * (-a)", false
            ),
            Arguments.of(
                "(a + b + c) * (a + b + c)", true
            ),
            Arguments.of(
                "(a + -b + -c) * (-a + -b + c)", true
            ),
            Arguments.of(
                "(a + b) * (-a + -b + c) * (-c)", true
            ),
            Arguments.of(
                "(a + b + -c) * (-a) * (a + b + c) * (a + -b)", false
            ),
            Arguments.of(
                "(b +-c) * (-a) * (a + c) * (a + -b) * (b + c)", false
            ),
            Arguments.of(
                "(a + b +-c) * (a + -b) * (b + c) * (-a + -b + -c) * (-a + -b + c) * (-a + b + -c)", false
            )
        )
    }
}