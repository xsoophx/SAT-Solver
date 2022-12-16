package de.tu_chemnitz.tdp_fiddle

data class Literal(
    val value: String,
    val isPositive: Boolean
) {
    fun negated(): Literal = Literal(value, !isPositive)

    override fun toString(): String {
        return when (this.isPositive) {
            true -> value
            else -> "-$value"
        }
    }
}

data class Clause(
    val literals: Set<Literal>
) : Set<Literal> by literals {
    constructor(vararg literals: Literal) : this(setOf(*literals))

    override fun toString() = literals.joinToString(prefix = "(", separator = " + ", postfix = ")") { "$it" }
}

enum class QuantifierType(val quantifier: Char) {
    UNIVERSAL('∀'),
    EXISTENTIAL('∃')
}

data class Quantifier(
    val type: QuantifierType,
    val variable: String,
    val isNegated: Boolean
)

typealias Predicate = Pair<Boolean, String>

//TODO: needs some better naming (and more thinking) again
data class Formula(
    val universal: Set<Quantifier>,
    val existential: Set<Quantifier>,
    val isNegated: Boolean,
    val predicate: Predicate,
    val freeVariables: Set<String>,
    val functions: Map<String, Int>
)
