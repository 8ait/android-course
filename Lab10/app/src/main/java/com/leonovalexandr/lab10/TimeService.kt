package com.leonovalexandr.lab10

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimeService : Service() {

    inner class MyBinder: Binder() {
        fun getService() : TimeService {
            return this@TimeService
        }
    }

    private var counter = 0
    private lateinit var job: Job

    private val myBinder = MyBinder()

    private var interval:Int = 1

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        counter = intent?.getIntExtra("counter", 0) ?: 0
        interval = intent?.getIntExtra("interval", 1) ?: 1
        job = GlobalScope.launch {
            while (true) {
                delay(interval * 1000L)
                Log.d("SERVICE", "Timer Is Ticking: " + counter)
                counter++
                val intent = Intent(BROADCAST_TIME_EVENT)
                intent.putExtra("counter", counter)
                sendBroadcast(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("SERVICE", "onDestroy")
        job.cancel()
        super.onDestroy()
    }
}