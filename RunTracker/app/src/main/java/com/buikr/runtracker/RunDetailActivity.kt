package com.buikr.runtracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.buikr.runtracker.databinding.ActivityRunDetailBinding
import com.buikr.runtracker.model.Run


class RunDetailActivity : AppCompatActivity() {
    companion object {
        val KEY_RUN = "KEY_RUN"
    }

    private lateinit var binding: ActivityRunDetailBinding
    private lateinit var run: Run

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        run = this.intent.getParcelableExtra(KEY_RUN)!!
        binding.toolbarLayout.title = run.title
//        binding.tvDistance.text = getString(R.string.distance_value, run.distance)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.run_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}