package com.buikr.runtracker.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.R
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.ActivityRunSessionBinding
import com.buikr.runtracker.service.LocationService
import com.buikr.runtracker.util.elapsedTime
import com.buikr.runtracker.util.formatToString
import com.buikr.runtracker.viewmodel.RunViewModel
import com.buikr.runtracker.viewmodel.SessionViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.color.MaterialColors
import java.util.*


class RunSessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunSessionBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var bound: Boolean = false
    private var locationService: LocationService? = null

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocationBinder
            locationService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDistanceValue.text = getString(R.string.distance_value, 0.0)
        binding.tvPaceValue.text = paceString(0.0, 0)

        binding.cmTime.start()
        binding.cmTime.setOnChronometerTickListener {
            locationService?.let { service ->
                binding.tvDistanceValue.text = getString(R.string.distance_value, service.distance)
                binding.tvPaceValue.text = paceString(service.distance,
                    (binding.cmTime.elapsedTime() / 1000).toInt()
                )
            }
        }

        binding.btTime.setOnClickListener(::timeButtonClick)


        window.statusBarColor = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorSurfaceVariant)

        startService(Intent(this, LocationService::class.java))
        bindService(Intent(this, LocationService::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        stopService(Intent(this, LocationService::class.java))
    }


    private fun timeButtonClick(v: View) {
        if (locationService != null && !locationService!!.locationList.isEmpty()) {
            showEndRunDialog()
        } else {
            showDiscardRunDialog()
        }
    }

    private fun saveRun() {
        val runViewModel = ViewModelProvider(this)[SessionViewModel::class.java]
        val today = Calendar.getInstance().time
        locationService?.distance?.let { distance ->
            runViewModel.insert(
                Run(
                    0,
                    "${today.formatToString("EEEE")} run",
                    today,
                    "",
                    (binding.cmTime.elapsedTime() / 1000).toInt(),
                    distance,
                    locationService!!.locationList.map { location ->
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

    private fun showEndRunDialog() {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(getString(R.string.end_run_title))
        alertDialog.setMessage(getString(R.string.end_run_message))
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

    private fun showDiscardRunDialog() {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(getString(R.string.discard_run_title))
        alertDialog.setMessage(getString(R.string.discard_run_message))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.yes)
        ) { dialog, which -> finish() }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    override fun onBackPressed() {
        showDiscardRunDialog()
    }
}