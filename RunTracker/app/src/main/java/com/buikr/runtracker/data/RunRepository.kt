package com.buikr.runtracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RunRepository(private val runDao: RunDao) {
    fun getAllRuns(): LiveData<List<Run>> {
        return runDao.getAllRuns().map { runs ->
            runs.map { roomRun ->
                roomRun.toDomainModel()
            }
        }
    }

    private fun RoomRun.toDomainModel(): Run {
        return Run(
            id = id,
            title = title,
            date = date,
            description = description,
            duration = duration,
            distance = distance,
            locationDataPath = locationDataPath
        )
    }

    private fun Run.toRoomModel(): RoomRun {
        return RoomRun(
            title = title,
            date = date,
            description = description,
            duration = duration,
            distance = distance,
            locationDataPath = locationDataPath
        )
    }

    suspend fun insert(run: Run) = withContext(Dispatchers.IO) {
        runDao.insertRun(run.toRoomModel())
    }

    suspend fun delete(run: Run) = withContext(Dispatchers.IO) {
        val roomRun = runDao.getRunById(run.id) ?: return@withContext
        runDao.deleteRun(roomRun)
    }

    suspend fun update(run: Run) = withContext(Dispatchers.IO) {
        val roomRun = runDao.getRunById(run.id)
        roomRun?.title = run.title
        roomRun?.description = run.description
        roomRun?.let { runDao.updateRun(roomRun) }
    }

    suspend fun getById(id: Long) = withContext(Dispatchers.IO) {
        runDao.getRunById(id)?.toDomainModel()
    }
}