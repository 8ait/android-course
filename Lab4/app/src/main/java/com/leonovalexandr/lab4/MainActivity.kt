package com.leonovalexandr.lab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private val answerCount = 4

    private val ranges = mapOf(
            Level.EASY to 0..5,
            Level.MEDIUM to 5..20,
            Level.HARD to 20..50
    )

    private val operationFuncs = listOf(
            Operation("+") { n1: Int, n2: Int -> n1 + n2},
            Operation("-") { n1: Int, n2: Int -> n1 - n2},
            Operation("*") { n1: Int, n2: Int -> n1 * n2}
    )

    private var rightCount = 0
    private var wrongCount = 0

    private var equation: Equation? = null
    private var answers = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startRound()
    }

    // Начать раунд.
    private fun startRound() {
        createEquation()
        generateAnswers()
        setScreen()
    }

    // Подготовить экран
    private fun setScreen(){

        val listView: ListView = findViewById(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Подготовить пример
        val equationView: TextView = findViewById(R.id.eq)
        equationView.text = equation?.text

        // Подготовить список
        val adapter = ArrayAdapter<Int>(this,
                android.R.layout.simple_list_item_1, answers)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            var answer: Int = parent.getItemAtPosition(position) as Int
            checkAnswer(answer)
        }

        // Подоговить иконку правильных ответов
        val rightView: TextView = findViewById(R.id.right)
        rightView.text = rightCount.toString()

        // Подготовить иконку неправильных ответов
        val wrongView: TextView = findViewById(R.id.wrong)
        wrongView.text = wrongCount.toString()
    }

    // Проверить ответ.
    private fun checkAnswer(number: Int){
        if (number == equation?.value){
            rightCount++
        } else {
            wrongCount++
        }
        startRound()
    }

    // Создать пример.
    private fun createEquation() {
        val operation = operationFuncs.random()
        val range = ranges[getLevel()] ?: 0..0
        equation = Equation(range, operation)
    }

    // Сгенерировать возможные ответы.
    private fun generateAnswers() {
        answers.clear()
        val rightAnswer = equation?.value
        if (rightAnswer != null){
            answers.add(rightAnswer)
            while(answers.count() != answerCount){
                val nextAnswer = Random.nextInt(rightAnswer - 10, rightAnswer + 10)
                if (answers.contains(nextAnswer))
                    continue
                answers.add(nextAnswer)
            }
        }
    }

    // Получить сложность раунда.
    private fun getLevel(): Level {
        if (rightCount == 0)
            return Level.EASY

        if (rightCount / (wrongCount + rightCount).toFloat() > 0.75)
            return Level.HARD

        if (rightCount / (wrongCount + rightCount).toFloat() > 0.50)
            return Level.MEDIUM

        return Level.EASY
    }
}