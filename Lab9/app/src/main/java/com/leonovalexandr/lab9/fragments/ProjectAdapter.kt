package com.leonovalexandr.lab9.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.leonovalexandr.lab9.R
import com.leonovalexandr.lab9.models.Project

class ProjectAdapter (context: Context, items: ArrayList<Project>): BaseAdapter() {

    var ctx: Context = context
    var objects: ArrayList<Project> = items
    var inflater: LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // Формирование сетки содержащих строку данных.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Если разметка ещё не существует, создаём её по шаблону
        var view = convertView
        if (view == null)
            view = inflater.inflate(R.layout.listview_layout_item, parent, false)
        // Получение объекта с информацией о студенте
        val s = objects[position]
        // Заполнение элементов данными из объекта
        var v = view?.findViewById(R.id.name) as TextView
        v.text = s.name
        return view
    }

    // Получение элемента данных в указанной строке
    override fun getItem(position: Int): Any {
        return objects[position]
    }

    // Получение идентификатора элемента в указанной строке
    // Часто вместо него возвращается позиция элемента
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    // Получение количества элементов в списке
    override fun getCount(): Int {
        return objects.size
    }
}