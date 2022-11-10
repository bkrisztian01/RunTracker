package com.buikr.runtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buikr.runtracker.RunTrackerApplication
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.data.RunRepository
import kotlinx.coroutines.launch

class RunViewModel : ViewModel() {
    private val repository: RunRepository

    val allRuns: LiveData<List<Run>>

    init {
        val runDao = RunTrackerApplication.runDatabase.runDao()
        repository = RunRepository(runDao)
        allRuns = repository.getAllRuns()
    }

    fun insert(run: Run) = viewModelScope.launch {
        repository.insert(run)
    }

    fun delete(run: Run) = viewModelScope.launch {
        repository.delete(run)
    }

    fun update(run: Run) = viewModelScope.launch {
        repository.update(run)
    }

    fun getById(id: Long): LiveData<Run?> {
        val result = MutableLiveData<Run?>()
        viewModelScope.launch {
            result.postValue(repository.getById(id))
        }
        return result
    }
}