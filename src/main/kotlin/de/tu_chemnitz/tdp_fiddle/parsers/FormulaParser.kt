package de.tu_chemnitz.tdp_fiddle.parsers

import de.tu_chemnitz.tdp_fiddle.Formula
import de.tu_chemnitz.tdp_fiddle.InvalidClauseException
import de.tu_chemnitz.tdp_fiddle.InvalidFormulaException
import de.tu_chemnitz.tdp_fiddle.Quantifier
import de.tu_chemnitz.tdp_fiddle.QuantifierType

object FormulaParser {
    private const val UNIVERSAL_QUANTIFIER = '∀'
    private const val EXISTENTIAL_QUANTIFIER = '∃'
    private const val NEGATOR = '-'
    private const val WHITESPACE = " "

    fun readInput(input: String): List<Formula> =
        input.trim().replace(WHITESPACE, "").splitToSequence("*", "+").mapIndexed { index, value ->
            try {
                createFormula(value)
            } catch (e: InvalidClauseException) {
                throw InvalidFormulaException(indexSubFormula = index, indexLiteral = e.index, e)
            }
        }.toList()

    private fun createFormula(formula: String): Formula {
        val quantifiers = formula.getQuantifiers()
        return Formula(
            universal = quantifiers.getValue(UNIVERSAL_QUANTIFIER),
            existential = quantifiers.getValue(EXISTENTIAL_QUANTIFIER),
            isNegated = false,
            predicate = false to "",
            freeVariables = setOf(),
            functions = emptyMap()
        )
    }

    private inline fun Map<Char, MutableSet<Quantifier>>.addVariable(
        type: QuantifierType?,
        current: String,
        isNegated: Boolean,
        message: () -> String
    ) {
        if (type != null) {
            if (current.isEmpty()) {
                throw IllegalStateException(message())
            }
            getValue(type.quantifier) += Quantifier(type = type, variable = current, isNegated = isNegated)
        }
    }

    private fun String.getQuantifiers(): Map<Char, Set<Quantifier>> {
        val quantifiers = mapOf(
            UNIVERSAL_QUANTIFIER to mutableSetOf<Quantifier>(),
            EXISTENTIAL_QUANTIFIER to mutableSetOf(),
        )

        var current = ""
        var type: QuantifierType? = null
        var negated = false

        for (char in this) {
            when (char) {
                UNIVERSAL_QUANTIFIER -> {
                    if (current.isNotEmpty()) {
                        quantifiers.addVariable(type, current, negated) { "Two quantifiers cannot follow each other." }
                        current = ""
                        negated = false
                    }
                    type = QuantifierType.UNIVERSAL
                }
                EXISTENTIAL_QUANTIFIER -> {
                    if (current.isNotEmpty()) {
                        quantifiers.addVariable(type, current, negated) { "Two quantifiers cannot follow each other." }
                        current = ""
                        negated = false
                    }
                    type = QuantifierType.EXISTENTIAL
                }
                NEGATOR -> {
                    if (current.isEmpty()) negated = true
                }
                else -> {
                    if (char.isUpperCase()) {
                        break
                    }
                    checkNotNull(type) { "Every variable needs a quantifier." }
                    current += char
                }
            }
        }

        if (type != null) {
            quantifiers.addVariable(type, current, negated) { "Two quantifiers cannot follow each other." }
        }

        return quantifiers
    }
}