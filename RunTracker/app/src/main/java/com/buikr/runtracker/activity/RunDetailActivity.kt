package com.buikr.runtracker.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.R
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.ActivityRunDetailBinding
import com.buikr.runtracker.fragment.EditRunDialogFragment
import com.buikr.runtracker.util.formatToString
import com.buikr.runtracker.viewmodel.DetailViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.color.MaterialColors


class RunDetailActivity : AppCompatActivity(), EditRunDialogFragment.EditRunDialogListener,
    OnMapReadyCallback {
    companion object {
        val KEY_RUN = "KEY_RUN"
    }

    private lateinit var binding: ActivityRunDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var run: Run

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        detailViewModel.getById(this.intent.getLongExtra(KEY_RUN, 0)).observe(this) { r ->
            this.run = r!!
            binding.toolbarLayout.title = run.title
            binding.tvDistanceValue.text = getString(R.string.distance_value, run.distance)
            val pace: Int = (run.duration / run.distance).toInt()
            binding.tvPaceValue.text = getString(R.string.pace_value, pace / 60, pace % 60)
            binding.tvTimeValue.text =
                getString(R.string.time_value, run.duration / 60, run.duration % 60)
            binding.tvDescription.text = run.description
            binding.tvDate.text = run.date.formatToString("MM/dd/yyyy - HH:mm")

            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }


        window.statusBarColor =
            MaterialColors.getColor(binding.root, android.R.attr.colorBackground)

        // Make scrollview not scrollable when panning on mapview
        binding.transparentImage.setOnTouchListener(OnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_UP -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.run_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.edit_run -> {
                val editRunFragment = EditRunDialogFragment()
                editRunFragment.startingName = run.title
                editRunFragment.startingDescription = run.description
                editRunFragment.show(supportFragmentManager, "TAG")
            }
            R.id.delete_run -> {
                val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Delete run?")
                alertDialog.setMessage("Are you sure you want to delete this run?")
                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    getString(R.string.yes)
                ) { dialog, which ->
                    detailViewModel.delete(run)
                    finish()
                }
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRunEdited(name: String, description: String) {
        run.title = name;
        run.description = description;
        detailViewModel.update(run)
        binding.toolbarLayout.title = run.title
        binding.tvDescription.text = run.description
    }

    override fun onMapReady(map: GoogleMap) {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            val style: MapStyleOptions =
                MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night)
            map.setMapStyle(style)
        }

        val builder = LatLngBounds.builder()
        val latLngs = run.locationData

        if (run.locationData.isEmpty()) return

        val polyline = PolylineOptions()
            .color(
                MaterialColors.getColor(
                    binding.root,
                    com.google.android.material.R.attr.colorTertiary
                )
            )

        for (latLng in latLngs) {
            polyline.add(latLng)
            builder.include(latLng)
        }

        val bounds = builder.build()

        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)

        map.addPolyline(polyline)

        map.setOnMapLoadedCallback {
            map.animateCamera(cu)
        }
    }
}