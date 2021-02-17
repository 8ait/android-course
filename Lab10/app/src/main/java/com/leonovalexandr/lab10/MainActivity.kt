package com.leonovalexandr.lab10

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.transition.Slide
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

const val BROADCAST_TIME_EVENT = "com.leonovalexandr.lab10.timeevent"

class MainActivity : AppCompatActivity() {

    var receiver: BroadcastReceiver? = null
    val filter = IntentFilter(BROADCAST_TIME_EVENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val intervalView = findViewById<TextView>(R.id.intervalValue)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                intervalView.text = "Интервал: $progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {    }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {     }
        })

        receiver = object : BroadcastReceiver() {
            // Получено широковещательное сообщение
            override fun onReceive(context: Context?, intent: Intent) {
                val counter = intent.getIntExtra("counter", 0)
                val textCounter = findViewById<TextView>(R.id.counter_view)
                textCounter.text = counter.toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun buttonStartService(view: View) {
        val intent = Intent(this, TimeService::class.java)

        val slider = findViewById<SeekBar>(R.id.seekBar)
        val intervalValue = slider.progress
        val editStart = findViewById<EditText>(R.id.startValue)
        val startValue = editStart.text.toString().toIntOrNull()

        intent.putExtra("interval", intervalValue)
        intent.putExtra("counter", startValue ?: 0)

        startService(intent)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        registerReceiver(receiver, filter)
    }

    fun buttonStopService(view: View) {
        unregisterReceiver(receiver)
        unbindService(myConnection)
        stopService(Intent(this, TimeService::class.java))
    }

    var myService: TimeService? = null
    var isBound = false
    val myConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimeService.MyBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }
}