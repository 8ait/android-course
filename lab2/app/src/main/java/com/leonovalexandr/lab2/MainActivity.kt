package com.leonovalexandr.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*
     * Обарабатывает вычисление результата.
     * @param calc Функция для вычилсения.
     */
    private fun calculate(calc: (n1: Float, n2: Float) -> Float) {
        val (n1, n2) = getNumbers()

        val textView: TextView = findViewById(R.id.result)
        if (n1 == null || n2 == null) {
            printError("не верный формат полей", textView)
            return
        }

        var error: String? = null
        var n: Float = 0f
        try {
            n = calc(n1, n2)
        }
        catch (e: Exception) {
            error = e.message
        }


        if (error != null){
            printError(error, textView)
        } else if (n.isNaN()) {
            printError("неверные данные", textView)
        } else {
            printResult(n, textView)
        }
    }

    /*
     * Получает числа с полей.
     * @return Пара чисел допускающих null.
     */
    private fun getNumbers(): Pair<Float?, Float?> {
        val edit1: EditText = findViewById(R.id.number1)
        val n1 = edit1.text.toString().toFloatOrNull()

        val edit2: EditText = findViewById(R.id.number2)
        val n2 = edit2.text.toString().toFloatOrNull()

        return Pair(n1, n2)
    }

    private fun printError(error: String, textView: TextView) {
        val resText = resources.getString(R.string.error_result)
        textView.text = String.format(resText, error)
    }

    private fun printResult(n: Float, textView: TextView){
        val resText = resources.getString(R.string.operation_result)
        textView.text = String.format(resText, n)
    }

    // Обработчик для кнопки сложения.
    fun addButtonClick(view: View) {
        calculate {x, y -> x + y}
    }

    // Обработчик для кнопки вычитания.
    fun minusButtonClick(view: View) {
        calculate {x, y -> x - y}
    }

    // Обработчик для кнопки произведения.
    fun productButtonClick(view: View) {
        calculate {x, y -> x * y}
    }

    // Обработчик для кнопки деления.
    fun quotientButtonClick(view: View) {
        calculate {x, y -> x / y}
    }
}