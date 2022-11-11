package com.buikr.runtracker

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.ActivityRunSessionBinding
import com.buikr.runtracker.service.LocationService
import com.buikr.runtracker.util.elapsedTime
import com.buikr.runtracker.util.formatToString
import com.buikr.runtracker.viewmodel.RunViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.util.*


class RunSessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunSessionBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var mBound: Boolean = false
    private var mService: LocationService? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocationService.LocationBinder
            mService = binder.getService()
            mBound = true

            Log.d("LOCATION", "A kurva anyad")
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDistanceValue.text = getString(R.string.distance_value, 0.0)
        binding.tvPaceValue.text = paceString(0.0, 0)

        binding.cmTime.start()
        binding.cmTime.setOnChronometerTickListener {
            mService?.let { service ->
                binding.tvDistanceValue.text = getString(R.string.distance_value, service.distance)
                binding.tvPaceValue.text = paceString(service.distance,
                    (binding.cmTime.elapsedTime() / 1000).toInt()
                )
            }
        }

        binding.btTime.setOnClickListener(::timeButtonClick)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.md_theme_light_surfaceVariant)

        startService(Intent(this, LocationService::class.java))
        bindService(Intent(this, LocationService::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        stopService(Intent(this, LocationService::class.java))
    }


    private fun timeButtonClick(v: View) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("End run?")
        alertDialog.setMessage("Are you sure you want to end your run?")
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            getString(R.string.yes)
        ) { dialog, which ->
            saveRun()
            finish()
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun saveRun() {
        val runViewModel = ViewModelProvider(this)[RunViewModel::class.java]
        val today = Calendar.getInstance().time
        mService?.distance?.let { distance ->
            runViewModel.insert(
                Run(
                    0,
                    "${today.formatToString("EEEE")} run",
                    today,
                    "",
                    (binding.cmTime.elapsedTime() / 1000).toInt(),
                    distance,
                    mService!!.locationList.map { location ->
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    }
                )
            )
        }

    }

    private fun paceString(distance: Double, elapsedTime: Int): String {
        if (distance == 0.0) {
            return getString(R.string.pace_value_null)
        }

        val pace = elapsedTime / distance

        return getString(R.string.pace_value, (pace / 60).toInt(), (pace % 60).toInt())
    }

    override fun onBackPressed() {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Discard run?")
        alertDialog.setMessage("Are you sure you want to discard your run?")
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.yes)
        ) { dialog, which -> finish() }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }
}