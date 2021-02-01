package com.leonovalexandr.lab4

import kotlin.random.Random
import kotlin.random.nextInt

// Пример
class Equation(range: IntRange, operation: Operation) {

    // Строка примера
    var text: String = ""
        get() {return "$n1${operation.name}$n2="}

    // Значение
    var value: Int = 0
        private set

    // Ответы
    var answers = ArrayList<Int>()

    // Первое число
    private var n1: Int = 0

    // Второе число
    private var n2: Int = 0

    // Операция
    private var operation: Operation = operation

    init {
        n1 = Random.nextInt(range)
        n2 = Random.nextInt(range)
        value = operation.calc(n1, n2)
    }
}
