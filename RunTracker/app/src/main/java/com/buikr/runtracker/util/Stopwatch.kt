package com.buikr.runtracker.util

class Stopwatch {
    private var elapsedTimeValue: Long = 0
    private var lastTime: Long = 0;
    val elapsedTime: Long
        get() {
            refreshElapsedTime()
            return elapsedTimeValue
        }
    var running: Boolean = false
        private set

    fun start() {
        if (running)
            return

        lastTime = System.currentTimeMillis()
        running = true
    }

    fun reset() {
        elapsedTimeValue = 0
        running = false
    }

    fun pause() {
        refreshElapsedTime()
        running = false
    }

    private fun refreshElapsedTime() {
        if (!running)
            return

        elapsedTimeValue += System.currentTimeMillis() - lastTime!!
        lastTime = System.currentTimeMillis()
    }
}