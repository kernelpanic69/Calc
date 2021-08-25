package com.github.arturnikolaenko.calc

import androidx.databinding.ObservableField
import com.github.arturnikolaenko.calc.util.isCloseBracket
import com.github.arturnikolaenko.calc.util.isDecimalSeparator
import com.github.arturnikolaenko.calc.util.isOpenBracket
import com.github.arturnikolaenko.calc.util.isOperator
import org.mariuszgromada.math.mxparser.Expression
import java.lang.StringBuilder

class Calculator {
    var text = ObservableField("")
    private val textVal: String
        get() = text.get()!!

    private var numOpenBrackets = 0
    private val exp = Expression()

    private val token = StringBuilder()

    private fun predictNextCharacter(ch: Char) = when {
        ch.isOperator() -> {
            if (ch in "*/^") {
                "1"
            } else {
                '0'
            }
        }
        ch.isDecimalSeparator() || ch.isOpenBracket() -> {
            "0"
        }
        else -> {
            ""
        }
    }

    private fun canAddChar(ch: Char): Boolean {
        if (ch.isLetter()) {
            return true
        }

        val numInitial = numOpenBrackets

        if (ch.isOpenBracket()) {
            numOpenBrackets++
        } else if (ch.isCloseBracket() && numOpenBrackets > 0) {
            numOpenBrackets--
        }

        val op = predictNextCharacter(ch)

        val s = textVal + ch + op + ")".repeat(numOpenBrackets)
        val oldExp = exp.expressionString
        exp.expressionString = s

        if (!exp.checkSyntax()) {
            println(exp.expressionString)
            numOpenBrackets = numInitial
            exp.expressionString = oldExp
            return false
        }

        println(s)
        exp.expressionString = s
        return true
    }

    fun addChar(ch: Char) {
        if (canAddChar(ch)) {
            text.set(textVal + ch)
        }
    }

    fun clear() {
        text.set("")
        numOpenBrackets = 0
    }

    fun backspace() {

        if (textVal.isNotEmpty()) {

            if (textVal.last().isOpenBracket()) {
                numOpenBrackets--
            } else if (textVal.last().isCloseBracket()) {
                numOpenBrackets++
            }

            val last = textVal.last()
            text.set(textVal.dropLast(1))

            if (textVal.isEmpty()) {
                clear()
            } else if (textVal.last().isDecimalSeparator()) {
                backspace()
            } else if (textVal.last().isLetter()) {
                if (last.isLetter() || last.isOpenBracket()) {
                    backspace()
                }
            }
        }
    }

    fun evaluate() {
        val x = exp.calculate()

        if (!x.isNaN()) {
            clear()

            var s = x.toString()

            if (s.endsWith(".0")) {
                s = s.dropLast(2)
            }

            text.set(s)
        }
    }

    fun addString(str: String) {
        for (ch in str) {
            addChar(ch)
        }
    }
}
