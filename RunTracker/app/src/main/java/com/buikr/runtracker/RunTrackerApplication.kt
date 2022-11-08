package com.buikr.runtracker

import android.app.Application
import androidx.room.Room
import com.buikr.runtracker.data.RunDatabase

class RunTrackerApplication : Application() {
    companion object {
        lateinit var runDatabase: RunDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        runDatabase = Room.databaseBuilder(
            applicationContext,
            RunDatabase::class.java,
            "run_database"
        ).fallbackToDestructiveMigration().build()
    }
}