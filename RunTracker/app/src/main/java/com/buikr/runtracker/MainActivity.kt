package com.buikr.runtracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.buikr.runtracker.adapter.RunItemRecyclerViewAdapter
import com.buikr.runtracker.databinding.ActivityMainBinding
import com.buikr.runtracker.model.Run
import java.util.*


class MainActivity : AppCompatActivity(), RunItemRecyclerViewAdapter.RunItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var runItemRecyclerViewAdapter: RunItemRecyclerViewAdapter


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
        for (i in 1..20) {
            runItemRecyclerViewAdapter.addItem(Run(1, "Monday run", Calendar.getInstance().time, "asd", 123, 1.23))
        }

        binding.searchviewRuns.setOnClickListener {
            binding.searchviewRuns.isIconified = false
        }
    }

    override fun onItemClick(run: Run) {
        val intent = Intent(this, RunDetailActivity::class.java)
        intent.putExtra(RunDetailActivity.KEY_RUN, run)
        startActivity(intent)
    }
}