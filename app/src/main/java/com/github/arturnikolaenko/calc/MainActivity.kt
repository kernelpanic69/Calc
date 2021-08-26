package com.github.arturnikolaenko.calc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.arturnikolaenko.calc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val calc = Calculator()
    private var isOnFunctions = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.calc = this.calc

        setupEventListeners()
        setContentView(binding.root)
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

    private fun setupEventListeners() {
        binding.mainButtons.apply {
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

            btnPercent.setOnClickListener(createListener('%'))

            btnOpPlus.setOnClickListener(createListener('+'))
            btnOpMinus.setOnClickListener(createListener('-'))
            btnOpMultiply.setOnClickListener(createListener('*'))
            btnOpDivide.setOnClickListener(createListener('/'))
            btnBracketLeft.setOnClickListener(createListener('('))
            btnBracketRight.setOnClickListener(createListener(')'))

            btnDecimal.setOnClickListener(createListener('.'))

            btnBackspace.setOnClickListener { calc.backspace() }
            btnBackspace.setOnLongClickListener { calc.clear();true }

            btnAnswer.setOnClickListener { calc.evaluate() }
        }

        binding.functionButtons.apply {
            btnCos.setOnClickListener(createListenerStr("cos("))
            btnSin.setOnClickListener(createListenerStr("sin("))
            btnTan.setOnClickListener(createListenerStr("tan("))
            btnPi.setOnClickListener(createListenerStr("pi"))

            btnExp.setOnClickListener(createListenerStr("e"))
            btnExpA.setOnClickListener(createListenerStr("e^"))
            btnLog.setOnClickListener(createListenerStr("log("))
            btnLn.setOnClickListener(createListenerStr("ln("))

            btnSqrt.setOnClickListener(createListenerStr("sqrt("))
            btnPowA.setOnClickListener(createListener('^'))
        }
    }

    private fun createListener(ch: Char): (View) -> Unit {
        return {
            calc.addChar(ch)
        }
    }

    private fun createListenerStr(str: String): (View) -> Unit {
        return {
            calc.addString(str)
        }
    }
}