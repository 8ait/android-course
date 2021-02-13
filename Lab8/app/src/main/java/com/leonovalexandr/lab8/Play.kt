package com.leonovalexandr.lab8

interface Play {
    fun getLevel(): Level
    fun setRight()
    fun setWrong()
    fun getScore(): Pair<Int, Int>
}