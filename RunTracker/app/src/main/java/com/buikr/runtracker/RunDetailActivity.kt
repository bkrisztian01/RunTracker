package com.buikr.runtracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.ActivityRunDetailBinding
import com.buikr.runtracker.fragments.EditRunDialogFragment
import com.buikr.runtracker.util.formatToString
import com.buikr.runtracker.viewmodel.RunViewModel


class RunDetailActivity : AppCompatActivity(), EditRunDialogFragment.EditRunDialogListener {
    companion object {
        val KEY_RUN = "KEY_RUN"
    }

    private lateinit var binding: ActivityRunDetailBinding
    private lateinit var runViewModel: RunViewModel
    private lateinit var run: Run

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        runViewModel = ViewModelProvider(this)[RunViewModel::class.java]

         runViewModel.getById(this.intent.getLongExtra(KEY_RUN, 0)).observe(this) { asd ->
            this.run = asd!!
            binding.toolbarLayout.title = run.title
            binding.tvDistanceValue.text = getString(R.string.distance_value, run.distance)
            val pace: Int = (run.duration / run.distance).toInt()
            binding.tvPaceValue.text = getString(R.string.pace_value, pace / 60, pace % 60)
            binding.tvTimeValue.text = getString(R.string.time_value, run.duration / 60, run.duration % 60)
            binding.tvDescription.text = run.description
            binding.tvDate.text = run.date.formatToString("MM/dd/yyyy - HH:mm")
        }
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
            R.id.edit_run -> {
                val editRunFragment = EditRunDialogFragment()
                editRunFragment.startingName = run.title
                editRunFragment.startingDescription = run.description
                editRunFragment.show(supportFragmentManager, "TAG")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRunEdited(name: String, description: String) {
        run.title = name;
        run.description = description;
        runViewModel.update(run)
        binding.toolbarLayout.title = run.title
        binding.tvDescription.text = run.description
    }


}