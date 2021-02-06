package com.leonovalexandr.lab5

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class ItemActivity : AppCompatActivity() {

    private var index = 0
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_activity)

        val intent = intent
        index = intent?.getIntExtra("index", -1) ?: -1
        item = intent?.getParcelableExtra("item") ?: Item()
        val editTitle = findViewById<EditText>(R.id.title)
        editTitle.setText(item.title)
        val editKind = findViewById<EditText>(R.id.kind)
        editKind.setText(item.kind)
        val editPrice = findViewById<EditText>(R.id.price)
        editPrice.setText(item.price.toString())
        val editWeight = findViewById<EditText>(R.id.weight)
        editWeight.setText(item.weight.toString())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_save) {

            this.item.title = findViewById<EditText>(R.id.title).
            text.toString()
            this.item.kind = findViewById<EditText>(R.id.kind).
            text.toString()
            this.item.price = findViewById<EditText>(R.id.price).
            text.toString().toDouble()
            this.item.weight = findViewById<EditText>(R.id.weight).text.toString().toDouble()

            if (check(this.item)){
                val intent = Intent()
                intent.putExtra("index", index)
                intent.putExtra("item", this.item)
                setResult(Activity.RESULT_OK, intent)

                finish()
                return true
            } else {
                var toast = Toast.makeText(applicationContext, "Неверный формат данных.", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.TOP, 0,0)
                toast.show()
                return false
            }

        }
        return super.onOptionsItemSelected(item)
    }

    // Поверка модели.
    private fun check(item: Item): Boolean {
        return item.title.isNotBlank() &&
                item.kind.isNotBlank() &&
                item.price >=0 &&
                item.weight >= 0
    }
}