package com.leonovalexandr.lab7

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import org.xmlpull.v1.XmlPullParser

// Адаптер для точки.
class PointAdapter(context: Context, items: ArrayList<Point>): BaseAdapter() {

    var ctx: Context = context
    var objects: ArrayList<Point> = items
    var inflater: LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // Формирование сетки содержащих строку данных.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Если разметка ещё не существует, создаём её по шаблону
        var view = convertView
        if (view == null)
            view = inflater.inflate(R.layout.listview_layout_point, parent, false)
        // Получение объекта с информацией о студенте
        val s = objects[position]
        // Заполнение элементов данными из объекта
        var v = view?.findViewById(R.id.item_name) as TextView
        v.text = s.name
        v = view.findViewById(R.id.item_location) as TextView
        v.text = "Latitude: ${s.location?.latitude} Longitude: ${s.location?.longitude}"
        var i = view.findViewById(R.id.item_distance) as ImageView
        i.setImageResource(if (s.isNear) R.drawable.green_oval else R.drawable.gray_oval)
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