package com.leonovalexandr.lab8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity(), OnDataListener, Play {

    private val operationFuncs = listOf(
        Operation("+") { n1: Int, n2: Int -> n1 + n2},
        Operation("-") { n1: Int, n2: Int -> n1 - n2},
        Operation("*") { n1: Int, n2: Int -> n1 * n2}
    )

    private var rightCount = 0
    private var wrongCount = 0

    private var isTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isTwoPane = findViewById<View>(R.id.frame_left) != null

        if (isTwoPane) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_left, LeftFragment())
                .add(R.id.frame_right, RightFragment(operationFuncs.first()))
                .commit()
        }
        else {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, LeftFragment())
                .commit()
        }
    }

    override fun onData(Data: Operation) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                if (isTwoPane)
                    R.id.frame_right
                else
                    R.id.container,
                RightFragment(Data)
            )
            .addToBackStack(null)
            .commit()
    }

    // Получить сложность раунда.
    override fun getLevel(): Level {
        if (rightCount == 0)
            return Level.EASY

        if (rightCount / (wrongCount + rightCount).toFloat() > 0.75)
            return Level.HARD

        if (rightCount / (wrongCount + rightCount).toFloat() > 0.50)
            return Level.MEDIUM

        return Level.EASY
    }

    override fun setRight() {
        rightCount++
    }

    override fun setWrong() {
        wrongCount++
    }

    override fun getScore(): Pair<Int, Int> {
        return Pair(rightCount, wrongCount)
    }


}