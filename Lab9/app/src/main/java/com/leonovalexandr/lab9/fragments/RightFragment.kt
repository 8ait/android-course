package com.leonovalexandr.lab9.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.leonovalexandr.lab9.R
import com.leonovalexandr.lab9.models.Project
import com.leonovalexandr.lab9.services.OnDataListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

// Фрагмент для отображения проекта.
class RightFragment(project: Project?) : Fragment() {

    private lateinit var mainContext: Context

    private lateinit var vv: View

    private val project: Project? = project

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_right, container, false)
        vv = view

        if (project != null) {
            val projectName = vv.findViewById<TextView>(R.id.project_name)
            projectName.text = project.name

            val projectFullName = vv.findViewById<TextView>(R.id.full_name)
            projectFullName.text = "Полное название: ${project.fullName}"

            val language = vv.findViewById<TextView>(R.id.language)
            language.text = "Язык разработки: ${project.language}"

            val ownerName = vv.findViewById<TextView>(R.id.owner_name)
            ownerName.text = project.owner?.name

            val projectDescription = vv.findViewById<TextView>(R.id.description)
            projectDescription.text = "Описание: ${project.description}"

            GlobalScope.launch {
                val url = URL(project.owner?.photoUrl)
                val c = url.openConnection() as HttpURLConnection
                var bitmap: Bitmap? = null
                if (c.responseCode == 200) {
                    val fin: InputStream = BufferedInputStream(c.inputStream)
                    val bytes = fin.readBytes()
                    bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.size)
                }
                c.disconnect()
                MainScope().launch {
                    val ownerImage = vv.findViewById<ImageView>(R.id.ivPhoto)
                    ownerImage.setImageBitmap(bitmap)
                }
            }
        } else {
            (vv.parent as ViewGroup).removeView(vv)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }
}