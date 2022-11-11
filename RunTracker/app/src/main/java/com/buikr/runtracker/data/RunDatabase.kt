package com.buikr.runtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.buikr.runtracker.util.DateConverter
import com.buikr.runtracker.util.LatLngListConverter

@Database(entities = [RoomRun::class], version = 1)
@TypeConverters(
    DateConverter::class,
    LatLngListConverter::class
)
abstract class RunDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao

    companion object {
        fun getDatabase(applicationContext: Context): RunDatabase {
            return Room.databaseBuilder(
                applicationContext,
                RunDatabase::class.java,
                "run-list"
            ).build();
        }
    }
}
