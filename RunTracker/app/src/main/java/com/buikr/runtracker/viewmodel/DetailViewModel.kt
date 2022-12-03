package com.buikr.runtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buikr.runtracker.RunTrackerApplication
import com.buikr.runtracker.data.Run
import com.buikr.runtracker.data.RunRepository
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel : ViewModel() {
    private val repository: RunRepository = RunTrackerApplication.runRepository

    fun getById(id: Long): MutableLiveData<Run?> {
        val result = MutableLiveData<Run?>()
        viewModelScope.launch {
            result.postValue(repository.getById(id))
        }
        return result
    }

    fun delete(run: Run) = viewModelScope.launch {
        repository.delete(run)
    }

    fun update(run: Run) = viewModelScope.launch {
        repository.update(run)
    }
}