package com.buikr.runtracker.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.formatToString(pattern: String): String {
    return SimpleDateFormat(pattern).format(this)
}