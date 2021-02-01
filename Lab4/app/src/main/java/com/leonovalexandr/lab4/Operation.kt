package com.leonovalexandr.lab4

// Тип операции над числами
class Operation(name: String, calc: (n1: Int, n2: Int) -> Int) {

    var name: String
    var calc: (n1: Int, n2: Int) -> Int

    init{
        this.name = name
        this.calc = calc
    }
}