package com.buikr.runtracker.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.RunRowBinding
import com.buikr.runtracker.util.formatToString
import java.util.*

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

    var allRuns: List<Run>? = null
    var itemClickListener: RunItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RunRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val run = this.getItem(position)

        holder.run = run

        holder.binding.tvRunName.text = run.title
        holder.binding.tvRunDate.text = run.date.formatToString("MM/dd/yyyy HH:mm")
    }

    fun filter(text: String) {
        if (text.isEmpty()) {
            submitList(allRuns)
            notifyDataSetChanged()
            return
        }

        allRuns?.let { allRuns ->
            var filteredList: List<Run> = listOf<Run>()
            var lText = text.lowercase(Locale.getDefault())
            for (run in allRuns) {
                if (run.title.lowercase(Locale.getDefault()).contains(text)
                ) {
                    filteredList = filteredList.plus(run)
                }
            }

            submitList(filteredList)
            notifyDataSetChanged()
        }
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