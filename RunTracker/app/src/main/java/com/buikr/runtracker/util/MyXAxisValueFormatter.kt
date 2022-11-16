package com.buikr.runtracker.util

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*


class MyXAxisValueFormatter: IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {

        val millis = value.toLong() * 86400000

        return Date(millis).formatToString("EEE")
    }
}