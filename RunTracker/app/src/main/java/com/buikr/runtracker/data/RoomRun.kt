package com.buikr.runtracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Run")
data class RoomRun(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: Date,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "duration") var duration: Int,          // in seconds
    @ColumnInfo(name = "distance") var distance: Double,       // in kilometers
    @ColumnInfo(name = "location_data_path") var locationDataPath: String
) { }