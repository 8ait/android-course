package com.leonovalexandr.lab8

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.collections.ArrayList
import kotlin.random.Random

class RightFragment(oper: Operation) : Fragment() {

    private val ranges = mapOf(
        Level.EASY to 0..5,
        Level.MEDIUM to 5..20,
        Level.HARD to 20..50
    )

    private val answerCount = 4

    private val operation = oper
    private lateinit var mainContext: Context

    private var answers = ArrayList<Int>()

    private lateinit var equation: Equation
    private lateinit var vv: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vv = inflater.inflate(R.layout.fragment_right, container, false)
        startRound()
        return vv
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }

    private fun startRound(){
        createEquation()
        generateAnswers()
        setScreen()
    }

    // Подготовить экран
    private fun setScreen(){

        val listView: ListView = vv.findViewById(R.id.listView)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Подготовить пример
        val equationView: TextView = vv.findViewById(R.id.eq)
        equationView.text = equation?.text

        // Подготовить список
        val adapter = ArrayAdapter<Int>(mainContext,
            android.R.layout.simple_list_item_1, answers)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            var answer: Int = parent.getItemAtPosition(position) as Int
            checkAnswer(answer)
        }

        var (right, wrong) = (mainContext as Play).getScore()

        // Подоговить иконку правильных ответов
        val rightView: TextView = vv.findViewById(R.id.right)
        rightView.text = right.toString()

        // Подготовить иконку неправильных ответов
        val wrongView: TextView = vv.findViewById(R.id.wrong)
        wrongView.text = wrong.toString()
    }

    // Создать пример.
    private fun createEquation() {
        val range = ranges[(mainContext as Play).getLevel()] ?: 0..0
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

    // Проверить ответ.
    private fun checkAnswer(number: Int){
        if (number == equation?.value){
            (mainContext as Play).setRight()
        } else {
            (mainContext as Play).setWrong()
        }
        startRound()
    }
}