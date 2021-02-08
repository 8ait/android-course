package com.leonovalexandr.lab5

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

class ItemActivity : AppCompatActivity() {

    private var index = 0
    private val INDEX_TO_DELETE = -2
    private lateinit var item: Item

    // Хранилище имени временного файла
    private var currentPhotoPath: String = ""

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

        if (item.photo != "") {
            val bmp = BitmapFactory.decodeFile(item.photo)
            findViewById<ImageView>(R.id.ivPhoto).setImageBitmap(bmp)
            currentPhotoPath = item.photo
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            val bmp = BitmapFactory.decodeFile(currentPhotoPath)
            val ivPhoto = findViewById<ImageView>(R.id.ivPhoto)
            ivPhoto.setImageBitmap(bmp)
        }
        else
            currentPhotoPath = ""
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
            this.item.photo = currentPhotoPath

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

        if (item.itemId == R.id.action_photo) {
            // Формирование имени временного файла
            val photoFile = File.createTempFile(
                    "photo",
                    ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            // Сохранение имени для будущего использования
            currentPhotoPath = photoFile.absolutePath
            // Получение провайдера файла, чтобы камера могла // записать файл в нашу частную папку
            val photoURI = FileProvider.getUriForFile(
                    this, BuildConfig.APPLICATION_ID, photoFile)
            // Формирование запроса на фото
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, 0)
            return true
        }

        if (item.itemId == R.id.action_delete) {
            val intent = Intent()
            intent.putExtra("index", INDEX_TO_DELETE)
            intent.putExtra("item", this.item)
            setResult(Activity.RESULT_OK, intent)

            finish()
            return true
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