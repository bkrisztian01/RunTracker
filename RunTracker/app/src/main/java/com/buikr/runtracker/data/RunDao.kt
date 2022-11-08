package com.buikr.runtracker.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {
    @Query("SELECT * FROM run")
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
}
