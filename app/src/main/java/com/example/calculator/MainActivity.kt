package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var workingTV: TextView
    private lateinit var resultTV: TextView
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workingTV = findViewById(R.id.workingTV)
        resultTV = findViewById(R.id.resultTV)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    workingTV.append(view.text)
                canAddDecimal = false
            }
            else
                workingTV.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            workingTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        workingTV.text = ""
        resultTV.text = ""
    }

    fun backSpaceAction(view: View) {
        val length = workingTV.length()
        if (length > 0)
            workingTV.text = workingTV.text.subSequence(0, length - 1)
    }

    fun equalsAction(view: View) {
        resultTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitOperators = digitOperators()
        if (digitOperators.isEmpty()) return ""

        val timesDivision = timeDivisionCalculate(digitOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculator(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculator(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in 1 until passedList.size step 2) {
            val operator = passedList[i] as Char
            val nextDigit = passedList[i + 1] as Float

            when (operator) {
                '+' -> result += nextDigit
                '-' -> result -= nextDigit
            }
        }
        return result
    }

    private fun timeDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float

                when (operator) {
                    '*' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i >= restartIndex) {
                newList.add(passedList[i])
            }
        }
        return newList
    }

    private fun digitOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingTV.text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit.isNotEmpty())
            list.add(currentDigit.toFloat())
        return list
    }
}
