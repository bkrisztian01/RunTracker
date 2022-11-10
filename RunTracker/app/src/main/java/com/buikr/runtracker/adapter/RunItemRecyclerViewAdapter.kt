package com.buikr.runtracker.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.buikr.runtracker.R
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

        val weekDayIcon = mapOf(
            Calendar.MONDAY to R.drawable.ic_mon,
            Calendar.TUESDAY to R.drawable.ic_tue,
            Calendar.WEDNESDAY to R.drawable.ic_wed,
            Calendar.THURSDAY to R.drawable.ic_thu,
            Calendar.FRIDAY to R.drawable.ic_fri,
            Calendar.SATURDAY to R.drawable.ic_sat,
            Calendar.SUNDAY to R.drawable.ic_sun,
        )
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

        val calendarDate = Calendar.getInstance()
        calendarDate.time = run.date
        holder.binding.ivRunIcon.setImageDrawable(ContextCompat.getDrawable(holder.binding.ivRunIcon.context, weekDayIcon[calendarDate.get(Calendar.DAY_OF_WEEK)]!!))
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
            var filteredList: List<Run> = listOf()
            val textLowercase = text.lowercase()
            for (run in allRuns) {
                if (run.title.lowercase().contains(textLowercase) ||
                    run.date.formatToString("MM/dd/yyyy HH:mm").contains(textLowercase)
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