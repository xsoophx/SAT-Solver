package de.tu_chemnitz.tdp_fiddle

data class Literal(
    val value: String,
    val isPositive: Boolean
) {
    fun negated(): Literal = Literal(value, !isPositive)
}

data class Clause(
    val literals: Set<Literal>
) : Set<Literal> by literals {
    constructor(vararg literals: Literal) : this(setOf(*literals))
}
