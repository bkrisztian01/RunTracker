package com.buikr.runtracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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

        runItemRecyclerViewAdapter = RunItemRecyclerViewAdapter()
        runItemRecyclerViewAdapter.itemClickListener = this
        binding.rvRuns.adapter = runItemRecyclerViewAdapter

        runViewModel = ViewModelProvider(this)[RunViewModel::class.java]
        runViewModel.allRuns.observe(this) { runs ->
            runItemRecyclerViewAdapter.allRuns = runs
            runItemRecyclerViewAdapter.submitList(runs)
        }

        binding.searchviewRuns.setOnClickListener {
            binding.searchviewRuns.isIconified = false
        }

        binding.searchviewRuns.setOnFocusChangeListener {
            v, focused ->
            if (!focused && "".equals(binding.searchviewRuns.query)) {
                binding.searchviewRuns.clearFocus()
            }
        }

        binding.searchviewRuns.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                runItemRecyclerViewAdapter.filter(qString);
                return true;
            }
            override fun onQueryTextSubmit(qString: String): Boolean {
                runItemRecyclerViewAdapter.filter(qString);
                return true;
            }
        })

        binding.fabRun.setOnClickListener {
            startActivity(Intent(this, RunSessionActivity::class.java))
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(R.color.md_theme_light_background)
    }

    override fun onItemClick(run: Run) {
        val intent = Intent(this, RunDetailActivity::class.java)
        intent.putExtra(RunDetailActivity.KEY_RUN, run.id)
        startActivity(intent)
    }
}