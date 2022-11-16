package com.buikr.runtracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.databinding.FragmentGraphBinding
import com.buikr.runtracker.util.MyXAxisValueFormatter
import com.buikr.runtracker.viewmodel.RunViewModel
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.color.MaterialColors
import java.util.*
import kotlin.math.floor


class GraphFragment : Fragment() {
    private lateinit var binding: FragmentGraphBinding
    private lateinit var chart: HorizontalBarChart
    private lateinit var runViewModel: RunViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(inflater)
        runViewModel = ViewModelProvider(this)[RunViewModel::class.java]
        setupChart()

        return binding.root
    }

    private fun setupChart() {
        val textColor = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorOnSecondaryContainer)
        chart = binding.chart

        chart.setDrawValueAboveBar(true)
        chart.setMaxVisibleValueCount(7)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)
        chart.description.isEnabled = false
        chart.setTouchEnabled(false)
        chart.legend.textColor =  textColor

        val xl = chart.xAxis
        xl.position = XAxisPosition.BOTTOM
        xl.setDrawAxisLine(true)
        xl.setDrawGridLines(false)
        xl.valueFormatter = MyXAxisValueFormatter()
        xl.granularity = 1f
        xl.textColor = textColor

        val yl = chart.axisLeft
        yl.granularity = 0.25f
        yl.setDrawLabels(true)
        yl.textColor = textColor

        val yr = chart.axisRight
        yr.setDrawAxisLine(false)
        yr.setDrawGridLines(false)
        yr.setDrawTopYLabelEntry(false)
        yr.setDrawLabels(false)

        chart.setFitBars(true);

        runViewModel.lastRuns.observe(viewLifecycleOwner) {
            setData(sumDistancesByDate(it))
        }
    }

    private fun sumDistancesByDate(runs: List<Run>): HashMap<Date, Double> {
        val hashMap = HashMap<Date, Double>()
        for (run in runs) {
            if (hashMap.containsKey(run.date)) {
                hashMap[run.date] = hashMap[run.date]!! + run.distance
            } else {
                hashMap.put(run.date, run.distance)
            }
        }

        return hashMap
    }

    private fun setData(sumOfDistances: HashMap<Date, Double>) {
        if (sumOfDistances.isEmpty())
            return

        val barWidth = 0.8f
        val values = mutableListOf<BarEntry>()

        for (entry in sumOfDistances)
            values.add(
                BarEntry(
                    (floor(entry.key.time / 86400000.0)).toFloat(),
                    entry.value.toFloat()
                )
            )

        val todaysDate = Calendar.getInstance().time
        chart.xAxis.axisMinimum = (floor(todaysDate.time / 86400000.0)).toFloat() - 7.5f
        chart.xAxis.axisMaximum = (floor(todaysDate.time / 86400000.0)).toFloat() + 0.5f

        val distanceSet: BarDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            distanceSet = chart.data.getDataSetByIndex(0) as BarDataSet
            distanceSet.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            distanceSet = BarDataSet(values, "Distance")
            distanceSet.setDrawIcons(false)
            context?.let {
                distanceSet.color = MaterialColors.getColor(binding.root, com.google.android.material.R.attr.colorSecondary)
            }
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(distanceSet)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.barWidth = barWidth
            chart.data = data
        }
    }
}