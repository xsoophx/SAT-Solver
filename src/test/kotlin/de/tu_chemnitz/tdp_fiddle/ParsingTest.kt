package de.tu_chemnitz.tdp_fiddle

import java.util.stream.Stream
import kotlin.test.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParsingTest {

    @ParameterizedTest
    @MethodSource("getInputs")
    fun `reads correct input`(input: String, expected: List<Clause>) {
        val actual = Main.readInput(input)
        assertEquals(expected = expected, actual = actual)
    }

    @ParameterizedTest
    @MethodSource("getClausesOfTwo")
    fun `reads correct clauses of two`(input: String, expected: List<Clause>) {
        val actual = Main.readInput(input, true)
        assertEquals(expected = expected, actual = actual)
    }

    companion object {
        private val a = Literal("a", true)
        private val b = Literal("b", true)
        private val c = Literal("c", true)
        private val d = Literal("d", true)
        private val g = Literal("g", true)
        private val notA = Literal("a", false)
        private val notB = Literal("b", false)
        private val notC = Literal("c", false)

        @JvmStatic
        private fun getInputs() = Stream.of(
            Arguments.of(
                "(a + b + c) * (a + b + c)",
                listOf(Clause(a, b, c), Clause(a, b, c))
            ),
            Arguments.of(
                "(-a + b + -c) * (a + -b + c)",
                listOf(Clause(notA, b, notC), Clause(a, notB, c))
            ),
            Arguments.of(
                "(-AB + CD + -CF) * (string + -dummy + value)",
                listOf(
                    Clause(Literal("AB", false), Literal("CD", true), Literal("CF", false)),
                    Clause(Literal("string", true), Literal("dummy", false), Literal("value", true)),
                )
            )
        )

        @JvmStatic
        fun getClausesOfTwo(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "(a + b) * (a + c) * (d + g)",
                listOf(Clause(a, b), Clause(a, c), Clause(d, g))
            ),
            Arguments.of(
                "(a + b + c) * (a + c + f) * (d + g + g)",
                listOf(Clause(a, b), Clause(a, c), Clause(d, g))
            )
        )
    }
}
