package de.tu_chemnitz_tdp_fiddle

import de.tu_chemnitz.tdp_fiddle.Clause
import de.tu_chemnitz.tdp_fiddle.Literal
import de.tu_chemnitz.tdp_fiddle.Main
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParsingTest {

    @Test
    fun `reads correct input`() {
        val input = "(a + b + c) * (a + b + c)"
        val actual = Main.readInput(input)

        val expected = listOf(
            Clause(Literal('a', true), Literal('b', true), Literal('c', true)),
            Clause(Literal('a', true), Literal('b', true), Literal('c', true)),
        )

        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun `reads correct negated input`() {
        val input = "(-a + b + -c) * (a + -b + c)"
        val actual = Main.readInput(input)

        val expected = listOf(
            Clause(Literal('a', false), Literal('b', true), Literal('c', false)),
            Clause(Literal('a', true), Literal('b', false), Literal('c', true)),
        )

        assertEquals(expected = expected, actual = actual)
    }
}
