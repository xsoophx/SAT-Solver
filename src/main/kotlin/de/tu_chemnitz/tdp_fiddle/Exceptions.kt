package de.tu_chemnitz.tdp_fiddle

import java.security.InvalidParameterException

class InvalidClauseException(val index: Int) : InvalidParameterException()
class InvalidFormulaException(val indexClause: Int, val indexLiteral: Int) : InvalidParameterException()