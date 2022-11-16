package com.buikr.runtracker.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.buikr.runtracker.R
import com.buikr.runtracker.databinding.ActivityMainBinding
import com.buikr.runtracker.fragments.GraphFragment
import com.buikr.runtracker.fragments.ListFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 100
    }

    private lateinit var binding: ActivityMainBinding

    private val listFragment = ListFragment()
    private val graphFragment = GraphFragment()
    private var activeFragment: Fragment = listFragment

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, listFragment, "RunList").commit()
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, graphFragment, "GraphList").hide(graphFragment).commit()

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

        binding.fabRun.setOnClickListener {
            checkLocationPermission()
        }

        binding.bottomNavigation.setOnItemSelectedListener(::onBottomNavItemSelected)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.md_theme_light_background)
    }

    private fun onBottomNavItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.runs -> {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(listFragment).commit();
                activeFragment = listFragment
                binding.fabRun.show()
                return true
            }
            R.id.graph -> {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(graphFragment).commit();
                activeFragment = graphFragment
                binding.fabRun.hide()
                return true
            }
        }
        return false
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
}