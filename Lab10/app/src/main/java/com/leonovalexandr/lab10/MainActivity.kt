package com.leonovalexandr.lab10

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.TextView

const val BROADCAST_TIME_EVENT = "com.leonovalexandr.lab10.timeevent"

class MainActivity : AppCompatActivity() {

    var receiver: BroadcastReceiver? = null
    val filter = IntentFilter(BROADCAST_TIME_EVENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        startService(Intent(this, TimeService::class.java))
        bindService(Intent(this, TimeService::class.java), myConnection, Context.BIND_AUTO_CREATE)
        registerReceiver(receiver, filter)
    }

    fun buttonStopService(view: View) {
        unregisterReceiver(receiver)
        unbindService(myConnection)
        stopService(Intent(this, TimeService::class.java))
    }

    fun buttonGetCounter(view: View){
        if(isBound)
            findViewById<TextView>(R.id.counter_view).text = myService!!.getCounter().toString()
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