package com.leonovalexandr.lab5

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private var items = ArrayList<Item>()
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Настройка списка
        val listView: ListView = findViewById(R.id.listItems)
        listView.adapter = ItemAdapter(this, items)
        listView.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("index", i)
            intent.putExtra("item", items[i])
            startActivityForResult(intent, 0)
        }

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            val index: Int = data?.getIntExtra("index", -1) ?: -1
            val item: Item = data?.getParcelableExtra("item") ?: Item()
            if (index != -1)
                items[index] = item
            else
                items.add(item)
            val listView: ListView = findViewById(R.id.listItems)
            (listView.adapter as ItemAdapter).notifyDataSetChanged()
        }
    }

}