package com.buikr.runtracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.activities.RunDetailActivity
import com.buikr.runtracker.adapter.RunItemRecyclerViewAdapter
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.FragmentListBinding
import com.buikr.runtracker.viewmodel.RunViewModel

class ListFragment : Fragment(), RunItemRecyclerViewAdapter.RunItemClickListener {
    private lateinit var binding: FragmentListBinding
    private lateinit var runItemRecyclerViewAdapter: RunItemRecyclerViewAdapter
    private lateinit var runViewModel: RunViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        initRecyclerViewAdapter()

        binding.searchviewRuns.setOnClickListener {
            binding.searchviewRuns.isIconified = false
        }

        binding.searchviewRuns.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                runItemRecyclerViewAdapter.filter(qString)
                return true;
            }

            override fun onQueryTextSubmit(qString: String): Boolean {
                runItemRecyclerViewAdapter.filter(qString)
                return true;
            }
        })

        return binding.root
    }

    private fun initRecyclerViewAdapter() {
        runItemRecyclerViewAdapter = RunItemRecyclerViewAdapter()
        runItemRecyclerViewAdapter.itemClickListener = this
        binding.rvRuns.adapter = runItemRecyclerViewAdapter

        runViewModel = ViewModelProvider(this)[RunViewModel::class.java]

        runViewModel.allRuns.observe(viewLifecycleOwner) { runs ->
            runItemRecyclerViewAdapter.allRuns = runs
            runItemRecyclerViewAdapter.submitList(runs)
        }
    }

    override fun onItemClick(run: Run) {
        val intent = Intent(context, RunDetailActivity::class.java)
        intent.putExtra(RunDetailActivity.KEY_RUN, run.id)
        startActivity(intent)
    }
}