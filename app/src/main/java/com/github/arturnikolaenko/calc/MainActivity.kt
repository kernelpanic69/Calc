package com.github.arturnikolaenko.calc

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.arturnikolaenko.calc.databinding.ActivityMainBinding
import com.github.arturnikolaenko.calc.util.displayMetrics

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val calc = Calculator()
    private var isOnFunctions = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.calc = this.calc

        setSupportActionBar(binding.toolbar)

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

    private fun toggleFunctions() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val mainButtons = binding.mainButtons.root
            val functions = binding.functionButtons.root
            val dm = displayMetrics()
            val duration = resources.getInteger(R.integer.material_motion_duration_medium_1)
            val btn = binding.btnSwitchControls

            isOnFunctions = if (isOnFunctions) {
                mainButtons.visibility = View.VISIBLE

                functions.animate()
                    .translationXBy(dm.width.toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                            functions.visibility = View.GONE
                        }
                    }).duration = duration.toLong()

                btn.animate().rotationYBy(180f)
                    .translationXBy(dm.width.toFloat() - btn.width)
                false
            } else {
                functions.visibility = View.VISIBLE

                functions.translationX = dm.width.toFloat()

                functions.animate().translationXBy(-dm.width.toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                            mainButtons.visibility = View.GONE
                        }
                    }).duration = duration.toLong()

                btn.animate()
                    .rotationYBy(180f)
                    .translationXBy(-dm.width.toFloat() + btn.width)

                true
            }
        }
    }

    override fun onBackPressed() {
        if (isOnFunctions) {
            toggleFunctions()
        } else {
            return super.onBackPressed()
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

        binding.btnSwitchControls.setOnClickListener {
            toggleFunctions()
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