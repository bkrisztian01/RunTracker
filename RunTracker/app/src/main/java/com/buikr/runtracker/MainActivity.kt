package com.buikr.runtracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.adapter.RunItemRecyclerViewAdapter
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.ActivityMainBinding
import com.buikr.runtracker.viewmodel.RunViewModel
import java.util.*


class MainActivity : AppCompatActivity(), RunItemRecyclerViewAdapter.RunItemClickListener {

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var runItemRecyclerViewAdapter: RunItemRecyclerViewAdapter
    private lateinit var runViewModel: RunViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scrollview.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            run {
                val dy = oldScrollY - scrollY
                if (dy < 0 && binding.fabRun.isExtended) {
                    binding.fabRun.shrink()
                } else if (dy > 0 && !binding.fabRun.isExtended) {
                    binding.fabRun.extend()
                }
            }
        }

        initRecyclerViewAdapter()

        binding.searchviewRuns.setOnClickListener {
            binding.searchviewRuns.isIconified = false
        }

        binding.searchviewRuns.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                runItemRecyclerViewAdapter.filter(qString);
                binding.scrollview.scrollY = 0
                return true;
            }

            override fun onQueryTextSubmit(qString: String): Boolean {
                runItemRecyclerViewAdapter.filter(qString);
                binding.scrollview.scrollY = 0
                return true;
            }
        })

        binding.fabRun.setOnClickListener {
            checkLocationPermission()
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.md_theme_light_background)
    }

    private fun initRecyclerViewAdapter() {
        runItemRecyclerViewAdapter = RunItemRecyclerViewAdapter()
        runItemRecyclerViewAdapter.itemClickListener = this
        binding.rvRuns.adapter = runItemRecyclerViewAdapter

        runViewModel = ViewModelProvider(this)[RunViewModel::class.java]

//        // For debugging
//        val latLngs = mutableListOf(
//            LatLng(31.0, 31.0),
//            LatLng(10.0, 31.0),
//            LatLng(10.0, 10.0),
//            LatLng(31.0, 131.0),
//            LatLng(31.0, 31.0),
//        )
//
//        runViewModel.insert(
//            Run(
//                0,
//                "Monday run",
//                Calendar.getInstance().time,
//                "Legyen mar vege \n\n\n\n\n\n\n\n\n\nIstenem",
//                123123,
//                1.23,
//                latLngs
//            )
//        )

        runViewModel.allRuns.observe(this) { runs ->
            runItemRecyclerViewAdapter.allRuns = runs
            runItemRecyclerViewAdapter.submitList(runs)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle(R.string.rationale_dialog_title)
                    .setMessage(R.string.location_permission_explanation)
                    .setPositiveButton(
                        R.string.button_ok
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            startRunningSession()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            PERMISSIONS_REQUEST_LOCATION
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Now check background location
                        startRunningSession()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied :(", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }
        }
    }

    private fun startRunningSession() {
        startActivity(Intent(this, RunSessionActivity::class.java))
    }

    override fun onItemClick(run: Run) {
        val intent = Intent(this, RunDetailActivity::class.java)
        intent.putExtra(RunDetailActivity.KEY_RUN, run.id)
        startActivity(intent)
    }
}