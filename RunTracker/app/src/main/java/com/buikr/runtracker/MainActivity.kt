package com.buikr.runtracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.adapter.RunItemRecyclerViewAdapter
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.ActivityMainBinding
import com.buikr.runtracker.viewmodel.RunViewModel
import java.util.*


class MainActivity : AppCompatActivity(), RunItemRecyclerViewAdapter.RunItemClickListener {
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

        var adapter = RunItemRecyclerViewAdapter()
        adapter.itemClickListener = this
        runItemRecyclerViewAdapter = adapter
        binding.rvRuns.adapter = runItemRecyclerViewAdapter

        runViewModel = ViewModelProvider(this)[RunViewModel::class.java]
        runViewModel.allRuns.observe(this) { runs ->
            runItemRecyclerViewAdapter.submitList(runs)
        }

//        runViewModel.insert(Run(
//            0,
//            "Monday run",
//            Calendar.getInstance().time,
//            "A very long description about the run.\n\n\n\nIt was very exhausting.",
//            123,
//            1.23,
//            ""
//        ))

        binding.searchviewRuns.setOnClickListener {
            binding.searchviewRuns.isIconified = false
        }
    }

    override fun onItemClick(run: Run) {
        val intent = Intent(this, RunDetailActivity::class.java)
        intent.putExtra(RunDetailActivity.KEY_RUN, run.id)
        startActivity(intent)
    }
}