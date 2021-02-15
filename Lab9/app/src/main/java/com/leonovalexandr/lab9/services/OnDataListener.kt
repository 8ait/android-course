package com.leonovalexandr.lab9.services

import com.leonovalexandr.lab9.models.Project

// Передача данных к фрагменту.
interface OnDataListener {

    fun onData(data: Project)
}