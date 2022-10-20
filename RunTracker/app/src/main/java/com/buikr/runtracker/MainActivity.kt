package com.buikr.runtracker

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.buikr.runtracker.adapter.RunItemRecyclerViewAdapter
import com.buikr.runtracker.databinding.ActivityMainBinding
import com.buikr.runtracker.databinding.RunRowBinding
import com.buikr.runtracker.model.Run

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var runItemRecyclerViewAdapter: RunItemRecyclerViewAdapter

    var oldPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runItemRecyclerViewAdapter =  RunItemRecyclerViewAdapter()
        binding.rvRuns.adapter = runItemRecyclerViewAdapter
        for (i in 1..20) {
            runItemRecyclerViewAdapter.addItem(Run(1, "Monday run", "asd"))
        }

        binding.fabRun.setOnClickListener {

        }
        binding.svMain.setOnScrollChangeListener { view, scrollX, scrollY, oldScrollX, oldScrollY ->

        }
    }
}