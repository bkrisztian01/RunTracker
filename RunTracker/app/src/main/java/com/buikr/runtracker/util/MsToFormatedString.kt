package com.buikr.runtracker.util

fun msToFormatedString(ms: Long) : String {
    val second: Long = ms / 1000 % 60
    val minute: Long = ms / (1000 * 60) % 60
    val hour: Long = ms / (1000 * 60 * 60) % 24

    return if (hour > 0)
        String.format("%02d:%02d:%02d", hour, minute, second)
    else
        String.format("%02d:%02d", minute, second)
}