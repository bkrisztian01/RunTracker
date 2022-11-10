package com.buikr.runtracker.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.buikr.runtracker.R
import com.buikr.runtracker.util.Stopwatch
import com.buikr.runtracker.util.msToFormatedString

// Please end my life
class LocationService : Service(), LocationListener {
    companion object {
        const val CHANNEL_ID = "RUNTRACKER_INSESSION"
        const val NOTIFICATION_ID = 101
    }

    private var mLocationManager: LocationManager? = null
    private var isLocationUpdateRunning: Boolean = false
    private val binder = LocationBinder()

    var locationList = mutableListOf<Location>()
    var distance = 0.0

    private val UPDATE_INTERVAL: Long = 1000    // 1.0 sec
    private val stopwatch = Stopwatch()
    private val mainHandler = Handler(Looper.getMainLooper())
    private val updateNotificationTask = object : Runnable {
        override fun run() {
            updateNotification("${msToFormatedString(stopwatch.elapsedTime)} - ${String.format("%.2f", distance)} km")
            mainHandler.postDelayed(this, 1000)
        }
    }

    inner class LocationBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    override fun onCreate() {
        stopwatch.start()
        mainHandler.post(updateNotificationTask)
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        locationList.clear()
        if (!isLocationUpdateRunning) {
            isLocationUpdateRunning = true
            mLocationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mLocationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                UPDATE_INTERVAL,
                0f,
                this
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        if (locationList.isNotEmpty()) {
            distance += location.distanceTo(locationList.last()).toDouble() / 1000
        }
        locationList += location
        Log.d("LOCATION", "onLocationChanged ----- location=$location")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(com.buikr.runtracker.R.string.session)
            val descriptionText = getString(com.buikr.runtracker.R.string.notification_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.running))
            .setContentText("00:00 - 0.0km")
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSmallIcon(com.buikr.runtracker.R.drawable.ic_run)
            .build()
        startForeground(NOTIFICATION_ID, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    /* Remove the locationlistener updates when Services is stopped */
    override fun onDestroy() {
        mainHandler.removeCallbacks(updateNotificationTask)

        try {
            stopLocationUpdates()
            stopForeground(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateNotification(text: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.running))
            .setContentText(text)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSmallIcon(com.buikr.runtracker.R.drawable.ic_run)
            .build()

        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun stopLocationUpdates() {
        isLocationUpdateRunning = false
        mLocationManager?.removeUpdates(this)
    }
}