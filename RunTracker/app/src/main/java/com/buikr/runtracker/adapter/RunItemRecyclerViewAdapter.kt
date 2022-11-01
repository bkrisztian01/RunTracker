package com.buikr.runtracker.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buikr.runtracker.databinding.RunRowBinding
import com.buikr.runtracker.model.Run
import com.buikr.runtracker.util.formatToString

class RunItemRecyclerViewAdapter :
    ListAdapter<Run, RunItemRecyclerViewAdapter.ViewHolder>(itemCallback) {
    companion object {
        object itemCallback : DiffUtil.ItemCallback<Run>() {
            override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var runList = emptyList<Run>()
    var itemClickListener: RunItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RunRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val run = runList[position]

        holder.run = run

        holder.binding.tvRunName.text = run.title
        holder.binding.tvRunDate.text = run.date.formatToString("MM/dd/yyyy HH:mm")
    }

    fun addItem(run: Run) {
        runList += run
        submitList(runList)
    }

    fun addAll(runs: List<Run>) {
        runList += runs
        submitList(runList)
    }

    fun deleteRow(position: Int) {
        runList = runList.filterIndexed { index, _ -> index != position }
        submitList(runList)
    }

    fun shuffleItems() {
        runList = runList.shuffled()
        submitList(runList)
    }

    inner class ViewHolder(val binding: RunRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var run: Run? = null

        init {
            itemView.setOnClickListener {
                run?.let { run -> itemClickListener?.onItemClick(run) }
            }
        }
    }

    interface RunItemClickListener {
        fun onItemClick(run: Run)
    }
}