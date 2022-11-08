package de.tu_chemnitz_tdp_fiddle

import de.tu_chemnitz.tdp_fiddle.Clause
import de.tu_chemnitz.tdp_fiddle.Literal
import de.tu_chemnitz.tdp_fiddle.Main
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

    companion object {
        @JvmStatic
        private fun getInputs() = Stream.of(
            Arguments.of(
                "(a + b + c) * (a + b + c)",
                listOf(
                    Clause(Literal("a", true), Literal("b", true), Literal("c", true)),
                    Clause(Literal("a", true), Literal("b", true), Literal("c", true)),
                )
            ),
            Arguments.of(
                "(-a + b + -c) * (a + -b + c)",
                listOf(
                    Clause(Literal("a", false), Literal("b", true), Literal("c", false)),
                    Clause(Literal("a", true), Literal("b", false), Literal("c", true)),
                )
            ),
            Arguments.of(
                "(-AB + CD + -CF) * (string + -dummy + value)",
                listOf(
                    Clause(Literal("AB", false), Literal("CD", true), Literal("CF", false)),
                    Clause(Literal("string", true), Literal("dummy", false), Literal("value", true)),
                )
            )
        )
    }
}
