package com.leonovalexandr.lab9.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.leonovalexandr.lab9.R
import com.leonovalexandr.lab9.models.Owner
import com.leonovalexandr.lab9.models.Project
import com.leonovalexandr.lab9.services.OnDataListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

// Фрагмент для отображения проектов гитхаб.
class LeftFragment : Fragment() {

    private lateinit var mainContext: Context

    private var projects = ArrayList<Project>()
    private lateinit var vv: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_left, container, false)
        vv = view

        val listOptions = vv.findViewById<ListView>(R.id.list_projects)
        listOptions.adapter = ProjectAdapter(mainContext, projects)
        listOptions.setOnItemClickListener { parent, view, position, id ->
            (mainContext as OnDataListener).onData(projects[position])
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }

    fun search(){
        val searchEdit = vv.findViewById<EditText>(R.id.searchEdit)
        val search = searchEdit.text
        if (search.isNullOrBlank())
            return
        projects.clear()
        GlobalScope.launch{
            try {
                val t = URL("https://api.github.com/search/repositories?q=${search}").readText()
                var json = JSONObject(t)
                val array = json.getJSONArray("items")
                for (i in 0 until array.length()) {
                    val project = mapProject(array.getJSONObject(i))
                    projects.add(project)
                }
                MainScope().launch {
                    val list = vv.findViewById<ListView>(R.id.list_projects)
                    (list.adapter as ProjectAdapter).notifyDataSetChanged()
                }
            } catch (exception: Exception) {
                Log.println(Log.ERROR, "Network:", exception.message)
            }
        }
    }

    // Смапить json в проект.
    private fun mapProject(json: JSONObject): Project{
        var project = Project()
        project.name = json.getString("name")
        project.description = json.getString("description")
        project.fullName = json.getString("full_name")
        project.language = json.getString("language")
        val owner = json.getJSONObject("owner")
        project.owner = Owner()
        project.owner!!.name = owner.getString("login")
        project.owner!!.photoUrl = owner.getString("avatar_url")
        return project
    }
}