package com.leonovalexandr.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    /*
     * Положение скролла.
     */
    private var scrollY: Int
        get() {
            val scroll = findViewById<ScrollView>(R.id.scroll)
            return scroll.scrollY
        }
        set(value) {
            val scroll = findViewById<ScrollView>(R.id.scroll)
            scroll.scrollY = value
        }

    /*
     * Массив чисел.
     */
    private var numbers = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            numbers = savedInstanceState.getIntegerArrayList("numbers") ?: ArrayList()
            for (item in numbers) {
                addTextView(item)
            }
            scrollY = savedInstanceState.getInt("scrollY")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("numbers", numbers)
        outState.putInt("scrollY", scrollY)
    }

    /*
     * Обработчик кнопки.
     */
    fun buttonAddClick(view: View){
        val randNumber = Random.nextInt(0, 100)
        addTextView(randNumber)
        numbers.add(randNumber)
    }

    /*
     * Добавление новой надписи.
     * @param number Число для отображения.
     */
    fun addTextView(number: Int) {
        val textView = TextView(this)
        textView.text = number.toString()
        textView.textSize = 24f
        val container = findViewById<LinearLayout>(R.id.innerContainer)
        container.addView(textView)
    }
}