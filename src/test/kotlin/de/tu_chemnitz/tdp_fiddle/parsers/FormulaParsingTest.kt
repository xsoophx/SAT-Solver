package de.tu_chemnitz.tdp_fiddle.parsers

import de.tu_chemnitz.tdp_fiddle.Formula
import de.tu_chemnitz.tdp_fiddle.Quantifier
import de.tu_chemnitz.tdp_fiddle.QuantifierType
import java.util.stream.Stream
import kotlin.test.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormulaParsingTest {

    @ParameterizedTest
    @MethodSource("getFormulas")
    fun `creates correct quantifiers from chained formulas`(formula: String, expected: List<Formula>) {
        val actual = FormulaParser.readInput(formula)

        expected.forEachIndexed { index, expectedFormula ->
            assertEquals(expected = expectedFormula.universal, actual[index].universal)
            assertEquals(
                expected = expectedFormula.existential,
                actual = actual[index].existential
            )
        }
    }

    companion object {
        private fun getDummyFormulaObject(
            universalQuantification: Set<Quantifier>,
            existentialQuantification: Set<Quantifier>,
        ) = Formula(
            universal = universalQuantification,
            existential = existentialQuantification,
            isNegated = false,
            predicate = false to "",
            freeVariables = setOf(),
            functions = mapOf()
        )

        @JvmStatic
        private fun getFormulas() = Stream.of(
            Arguments.of(
                "∀z∃y P(x, g(y), z)",
                listOf(
                    getDummyFormulaObject(
                        universalQuantification = setOf(Quantifier(QuantifierType.UNIVERSAL, "z", isNegated = false)),
                        existentialQuantification = setOf(
                            Quantifier(QuantifierType.EXISTENTIAL, "y", isNegated = false)
                        )
                    )
                )
            ),
            Arguments.of(
                "∀z∃y P(x, g(y), z) + -∀xQ(x) * -∀z∃x-R(f(x, z),z)",
                listOf(
                    getDummyFormulaObject(
                        universalQuantification = setOf(
                            Quantifier(QuantifierType.UNIVERSAL, "z", isNegated = false)
                        ),
                        existentialQuantification = setOf(
                            Quantifier(QuantifierType.EXISTENTIAL, "y", isNegated = false)
                        )
                    ),
                    getDummyFormulaObject(
                        universalQuantification = setOf(
                            Quantifier(QuantifierType.UNIVERSAL, "x", isNegated = true)
                        ),
                        existentialQuantification = emptySet()
                    ),
                    getDummyFormulaObject(
                        universalQuantification = setOf(
                            Quantifier(
                                QuantifierType.UNIVERSAL, "z", isNegated = true
                            )
                        ),
                        existentialQuantification = setOf(
                            Quantifier(QuantifierType.EXISTENTIAL, "x", isNegated = false)
                        )
                    ),
                )
            )
        )
    }
}