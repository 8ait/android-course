package com.leonovalexandr.lab7

import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class PointActivity : AppCompatActivity() {

    private var index = 0
    private lateinit var point: Point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        val intent = intent
        index = intent?.getIntExtra("index", -1) ?: -1
        point = intent?.getParcelableExtra("point") ?: Point()
        val editName = findViewById<EditText>(R.id.name)
        editName.setText(point.name)
        val editLatitude = findViewById<EditText>(R.id.latitude)
        editLatitude.setText((point.location?.latitude ?: 0).toString())
        val editLongitude = findViewById<EditText>(R.id.longitude)
        editLongitude.setText((point.location?.longitude ?: 0).toString())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_point, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_save) {

            this.point.name = findViewById<EditText>(R.id.name).text.toString()
            var lat = findViewById<EditText>(R.id.latitude).text.toString().toDouble()
            var lon = findViewById<EditText>(R.id.longitude).text.toString().toDouble()

            this.point.location = Location("")
            this.point.location?.latitude = lat
            this.point.location?.longitude = lon

            if (check(this.point)){
                val intent = Intent()
                intent.putExtra("index", index)
                intent.putExtra("point", this.point)
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

    private fun check(point: Point): Boolean{
        return !point.name.isNullOrBlank()
    }
}