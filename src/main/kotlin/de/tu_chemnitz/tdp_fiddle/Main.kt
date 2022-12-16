package de.tu_chemnitz.tdp_fiddle

import de.tu_chemnitz.tdp_fiddle.parsers.CNFParser

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            CNFParser.readInput(readln())
        } catch (e: InvalidFormulaException) {
            println("Wrong character in clause ${e.indexSubFormula} at literal ${e.indexLiteral}.")
        }
    }


}
