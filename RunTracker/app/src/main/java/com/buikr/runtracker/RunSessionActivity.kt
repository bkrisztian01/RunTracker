package com.buikr.runtracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.buikr.runtracker.databinding.ActivityRunSessionBinding


class RunSessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunSessionBinding
    private var distance: Double = 0.0
    private var elapsedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDistanceValue.text = getString(R.string.distance_value, distance)
        binding.tvPaceValue.text = paceString()

        binding.cmTime.start()
        binding.btTime.setOnClickListener(::timeButtonClick)


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.md_theme_light_surfaceVariant)
    }

    private fun timeButtonClick(v: View) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("End run?")
        alertDialog.setMessage("Are you sure you want to end your run?")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes)) { dialog, which -> }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun paceString(): String {
        if (distance == 0.0) {
            return getString(R.string.pace_value_null)
        }

        val pace = elapsedTime / distance

        return getString(R.string.pace_value, pace / 60, pace % 60)
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