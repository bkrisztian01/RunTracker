package com.buikr.runtracker.util

import android.os.SystemClock
import android.widget.Chronometer

fun Chronometer.elapsedTime(): Long {
    return SystemClock.elapsedRealtime() - base
}