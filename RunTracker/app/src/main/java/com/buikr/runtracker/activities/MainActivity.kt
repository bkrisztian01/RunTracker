package com.buikr.runtracker.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.buikr.runtracker.R
import com.buikr.runtracker.databinding.ActivityMainBinding
import com.buikr.runtracker.fragments.GraphFragment
import com.buikr.runtracker.fragments.ListFragment
import com.google.android.material.color.MaterialColors
import permissions.dispatcher.*
import java.util.*

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val listFragment = ListFragment()
    private val graphFragment = GraphFragment()
    private var activeFragment: Fragment = listFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, listFragment, "RunList").commit()
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, graphFragment, "Graph").hide(graphFragment)
            .commit()

        binding.scrollview.setOnScrollChangeListener(::onScrollChange)

        binding.fabRun.setOnClickListener { startRunningSessionWithPermissionCheck() }

        binding.bottomNavigation.setOnItemSelectedListener(::onBottomNavItemSelected)

        window.statusBarColor = MaterialColors.getColor(binding.root, android.R.attr.colorBackground)
    }

    private fun onScrollChange(
        v: View,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        run {
            val dy = oldScrollY - scrollY
            if (dy < 0 && binding.fabRun.isExtended) {
                binding.fabRun.shrink()
            } else if (dy > 0 && !binding.fabRun.isExtended) {
                binding.fabRun.extend()
            }
        }
    }

    private fun onBottomNavItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.runs -> {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(listFragment)
                    .commit();
                activeFragment = listFragment
                binding.fabRun.show()
                return true
            }
            R.id.graph -> {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(graphFragment)
                    .commit();
                activeFragment = graphFragment
                binding.fabRun.hide()
                return true
            }
        }
        return false
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun startRunningSession() {
        startActivity(Intent(this, RunSessionActivity::class.java))
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onCallDenied() {
        Toast.makeText(this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT)
            .show()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.location_rationale_title))
            .setMessage(R.string.location_permission_explanation)
            .setCancelable(false)
            .setPositiveButton(R.string.button_ok) { dialog, id -> request.proceed() }
            .setNegativeButton(R.string.exit) { dialog, id -> request.cancel() }
            .create()
        alertDialog.show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationNeverAskAgain() {
        Toast.makeText(
            this,
            getString(R.string.location_permission_never_ask_again),
            Toast.LENGTH_LONG
        )
            .show()
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", this.packageName, null),
            ),
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }
}