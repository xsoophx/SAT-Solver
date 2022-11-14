package de.tu_chemnitz.tdp_fiddle

import java.util.stream.Stream
import kotlin.test.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SATSolverTest {

    @ParameterizedTest
    @EnumSource(Method::class)
    fun `empty formula is solvable`(method: Method) {
        val emptyFormula = setOf<Clause>()
        assertEquals(expected = true, actual = SATSolver.isSolvable(emptyFormula, method))
    }

    @ParameterizedTest
    @EnumSource(Method::class)
    fun `formulas with empty clauses are not solvable`(method: Method) {
        val emptyClause = setOf(Clause(emptySet()))
        assertEquals(expected = false, actual = SATSolver.isSolvable(emptyClause, method))
    }

    @ParameterizedTest
    @MethodSource("getInputByCNF")
    fun `gets formulas correctly with dp`(cnf: String, expected: Boolean) {
        val actual = SATSolver.isSolvable(Main.readInput(cnf).toSet(), Method.DP)
        assertEquals(expected = expected, actual = actual)
    }

    @ParameterizedTest
    @MethodSource("getInputByCNF")
    fun `gets formulas correctly with resolution`(cnf: String, expected: Boolean) {
        val actual = SATSolver.isSolvable(Main.readInput(cnf).toSet(), Method.RESOLUTION)
        assertEquals(expected = expected, actual = actual)
    }

    companion object {
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
            )
        )
    }
}