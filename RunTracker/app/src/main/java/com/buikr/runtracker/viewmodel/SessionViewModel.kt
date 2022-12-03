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

class SessionViewModel : ViewModel() {
    private val repository: RunRepository = RunTrackerApplication.runRepository

    fun insert(run: Run) = viewModelScope.launch {
        repository.insert(run)
    }
}