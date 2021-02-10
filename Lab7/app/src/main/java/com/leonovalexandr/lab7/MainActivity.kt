package com.leonovalexandr.lab7

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val DISTANCE = 100f

    private var items = ArrayList<Point>()
    private var locationManager: LocationManager? = null
    private lateinit var listView: ListView

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            showInfo(location)
            updatePoints(location)
        }
        override fun onProviderDisabled(provider: String) { showInfo() }
        override fun onProviderEnabled(provider: String) { showInfo() }
        override fun onStatusChanged(provider: String, status: Int,
                                     extras: Bundle) { showInfo() }
    }

    private val MY_PERMISSIONS_REQUEST_LOCATION = 1

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            showMessage("Разрешение получено.")
        }
        else {
            showMessage("Разрешение не получено.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Настройка списка
        listView = findViewById(R.id.listItems)

        listView.adapter = PointAdapter(this, items)
        listView.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val intent = Intent(this, PointActivity::class.java)
            intent.putExtra("index", i)
            intent.putExtra("point", items[i])
            startActivityForResult(intent, 0)
        }

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, PointActivity::class.java)
            startActivityForResult(intent, 0)
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.show_geo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId == R.id.action_geo) {
            showPermissions()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        startTracking()
    }

    override fun onPause() {
        super.onPause()
        stopTracking()
    }

    private fun showPermissions() {
        // Проверяем есть ли разрешение
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Разрешения нет. Нужно ли показать пользователю пояснения?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                showMessage("Приложению требуется доступ к геолокации.")
            }
            else {
                // Пояснений не требуется, запрашиваем разрешение
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }
        showMessage("У приложения есть доступ к геолокации.")
    }

    private fun startTracking() {
        // Проверяем есть ли разрешение
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showPermissions()
        }
        else {
            locationManager!!.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 10f, locationListener)
            locationManager!!.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 1000, 10f, locationListener)
            showInfo()
        }
    }

    private fun stopTracking() {
        locationManager!!.removeUpdates(locationListener)
    }

    // Обновить информацию на экране.
    private fun showInfo(location: Location? = null) {
        val isGpsOn = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkOn = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        Log.println(Log.INFO, "GEO", if (isGpsOn) "GPS ON" else "GPS OFF")
        Log.println(Log.INFO, "GEO", if (isNetworkOn) "Network ON" else "Network OFF")
        if (location != null) {
            if (location.provider == LocationManager.GPS_PROVIDER) {
                Log.println(Log.INFO, "GEO", "GPS: широта = " + location.latitude.toString() + ", долгота = " + location.longitude.toString())
            }
            if (location.provider == LocationManager.NETWORK_PROVIDER) {
                Log.println(Log.INFO, "GEO", "NETWORK: широта = " + location.latitude.toString() + ", долгота = " + location.longitude.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            val index: Int = data?.getIntExtra("index", -1) ?: -1
            val item: Point = data?.getParcelableExtra("point") ?: Point()
            if (index != -1)
                items[index] = item
            else
                items.add(item)
            val listView: ListView = findViewById(R.id.listItems)
            (listView.adapter as PointAdapter).notifyDataSetChanged()
        }
    }

    private fun updatePoints(location: Location?){
        if (location != null){
            for (item in items){
                item.isNear = location.distanceTo(item.location) <= DISTANCE
            }
            (listView.adapter as PointAdapter).notifyDataSetChanged()
        }
    }

    // Показать сообщение с помощью toast.
    private fun showMessage(message: String){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }
}