package com.buikr.runtracker

import android.app.Application
import androidx.room.Room
import com.buikr.runtracker.data.RunDatabase
import com.buikr.runtracker.data.RunRepository

class RunTrackerApplication : Application() {
    companion object {
        lateinit var runDatabase: RunDatabase
        lateinit var runRepository: RunRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()

        runDatabase = Room.databaseBuilder(
            applicationContext,
            RunDatabase::class.java,
            "run_database"
        ).fallbackToDestructiveMigration().build()
        runRepository = RunRepository(runDatabase.runDao())
    }
}