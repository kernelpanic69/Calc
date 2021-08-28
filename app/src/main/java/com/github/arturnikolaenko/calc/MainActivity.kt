package com.github.arturnikolaenko.calc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableChar
import androidx.databinding.ObservableField
import com.github.arturnikolaenko.calc.databinding.ActivityMainBinding
import com.github.arturnikolaenko.calc.util.isDecimalSeparator
import com.github.arturnikolaenko.calc.util.isOperator
import kotlin.math.pow

class TextData {
    val currentValue = ObservableField("")
    val history = ObservableField("")
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    private var lastValue = .0
    private var lastAnswer = .0

    val data = TextData()

    private val history
        get() = data.history

    private val currentValue
        get() = data.currentValue

    private val currentOperator = ObservableChar('+')

    private val operators = mapOf<Char, (Double, Double) -> Double>(
        '+' to Double::plus,
        '-' to Double::minus,
        '*' to Double::times,
        '/' to Double::div,
        '^' to Double::pow,
        '%' to { x, y -> y / x * 100 }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setupEventListeners()
        binding.data = data
        setContentView(binding.root)
    }

    private fun setupEventListeners() {
        binding.apply {
            btnNum0.setOnClickListener(createListener('0'))
            btnNum1.setOnClickListener(createListener('1'))
            btnNum2.setOnClickListener(createListener('2'))
            btnNum3.setOnClickListener(createListener('3'))
            btnNum4.setOnClickListener(createListener('4'))
            btnNum5.setOnClickListener(createListener('5'))
            btnNum6.setOnClickListener(createListener('6'))
            btnNum7.setOnClickListener(createListener('7'))
            btnNum8.setOnClickListener(createListener('8'))
            btnNum9.setOnClickListener(createListener('9'))
            btnDecimal.setOnClickListener(createListener('.'))

            btnOpPlus.setOnClickListener(createListener('+'))
            btnOpMinus.setOnClickListener(createListener('-'))
            btnOpMultiply.setOnClickListener(createListener('*'))
            btnOpDivide.setOnClickListener(createListener('/'))
            btnOpPercent.setOnClickListener(createListener('%'))

            btnAnswer.setOnClickListener(::answer)
            btnBackspace.setOnClickListener(::backspace)
        }
    }

    private fun answer(v: View? = null) {
        val op = operators[currentOperator.get()]
        val curr = getCurrentDouble()

        val res = when {
            op != null -> op(lastValue, curr)
            else -> .0
        }

        lastAnswer = res
        history.set(lastAnswer.toString())
        currentValue.set("")
    }

    private fun getCurrentDouble(): Double {
        val str = currentValue.get()!!

        return when {
            str.isEmpty() -> .0
            str.last().isDecimalSeparator() -> str.dropLast(1).toDouble()
            else -> str.toDouble()
        }
    }

    private fun createListener(ch: Char) = { _: View ->
        when {
            ch.isDigit() -> {
                currentValue.set(currentValue.get() + ch)
            }
            ch.isDecimalSeparator() -> {
                if ('.' !in currentValue.get()!!) {
                    currentValue.set(currentValue.get() + ch)
                }
            }
            ch.isOperator() -> {
                answer()
                currentOperator.set(ch)
                lastValue = getCurrentDouble()
            }
        }
    }

    private fun backspace(v: View? = null) {
        val str = currentValue.get()!!

        if (str.isEmpty()) return

        currentValue.set(str.dropLast(1))

        if (str.last() == '.') {
            backspace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}