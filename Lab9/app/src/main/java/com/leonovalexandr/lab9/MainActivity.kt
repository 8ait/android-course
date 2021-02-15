package com.leonovalexandr.lab9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.leonovalexandr.lab9.fragments.LeftFragment
import com.leonovalexandr.lab9.fragments.RightFragment
import com.leonovalexandr.lab9.models.Project
import com.leonovalexandr.lab9.services.OnDataListener

class MainActivity : AppCompatActivity(), OnDataListener {

    private var isTwoPane = false

    private lateinit var leftFragment: LeftFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isTwoPane = findViewById<View>(R.id.frame_left) != null

        leftFragment = LeftFragment()
        if (isTwoPane) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_left, leftFragment)
                .add(R.id.frame_right, RightFragment(null))
                .commit()
        }
        else {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, leftFragment)
                .commit()
        }
    }

    override fun onData(data: Project) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                if (isTwoPane)
                    R.id.frame_right
                else
                    R.id.container,
                RightFragment(data)
            )
            .addToBackStack(null)
            .commit()
    }

    fun search(view: View){
        leftFragment.search()
    }
}