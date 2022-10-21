package com.buikr.runtracker

import android.R
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.buikr.runtracker.adapter.RunItemRecyclerViewAdapter
import com.buikr.runtracker.databinding.ActivityMainBinding
import com.buikr.runtracker.model.Run
import java.time.LocalDate
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var runItemRecyclerViewAdapter: RunItemRecyclerViewAdapter

    var oldPosition = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runItemRecyclerViewAdapter =  RunItemRecyclerViewAdapter()
        binding.rvRuns.adapter = runItemRecyclerViewAdapter
        for (i in 1..20) {
            runItemRecyclerViewAdapter.addItem(Run(1, "Monday run", LocalDate.now(), "asd"))
        }


        binding.searchviewRuns.setOnClickListener {
            binding.searchviewRuns.isIconified = false;
        }
    }
}