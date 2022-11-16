package com.buikr.runtracker.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {
    @Query("SELECT * FROM run ORDER BY date DESC")
    fun getAllRuns(): LiveData<List<RoomRun>>

    @Insert
    fun insertRun(run: RoomRun): Long

    @Update
    fun updateRun(run: RoomRun)

    @Delete
    fun deleteRun(run: RoomRun)

    @Query("SELECT * FROM run WHERE id = :id")
    fun getLiveDataRunById(id: Long?): LiveData<RoomRun?>

    @Query("SELECT * FROM run WHERE id = :id")
    fun getRunById(id: Long?): RoomRun?

    @Query("SELECT * FROM run WHERE :from <= date AND date <= :to ORDER BY date")
    fun getSevenRunsBetweenDates(from: Long, to: Long): LiveData<List<RoomRun>>
}
