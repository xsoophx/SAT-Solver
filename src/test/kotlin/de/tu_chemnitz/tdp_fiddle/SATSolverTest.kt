package de.tu_chemnitz.tdp_fiddle

import java.util.stream.Stream
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SATSolverTest {

    @Test
    fun `empty formula is solvable`() {
        val emptyFormula = setOf<Clause>()
        assertEquals(expected = true, actual = SATSolver.isSolvable(emptyFormula))
    }

    @Test
    fun `formulas with empty clauses are not solvable`() {
        val emptyClause = setOf(Clause(emptyList()))
        assertEquals(expected = false, actual = SATSolver.isSolvable(emptyClause))
    }

    @ParameterizedTest
    @MethodSource("getsInputByCNF")
    fun `gets formulas correctly`(cnf: String, expected: Boolean) {
        val actual = SATSolver.isSolvable(Main.readInput(cnf).toSet())
        assertEquals(expected = expected, actual = actual)
    }

    companion object {
        @JvmStatic
        private fun getsInputByCNF() = Stream.of(
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