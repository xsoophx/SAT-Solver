package de.tu_chemnitz.tdp_fiddle

class InvalidClauseException(val index: Int, cause: Throwable? = null) : IllegalArgumentException(cause)
class InvalidFormulaException(val indexClause: Int, val indexLiteral: Int, cause: Throwable? = null) : IllegalArgumentException(cause)