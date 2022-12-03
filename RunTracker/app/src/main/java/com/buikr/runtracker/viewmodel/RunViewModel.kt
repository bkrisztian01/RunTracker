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

class RunViewModel : ViewModel() {
    private val repository: RunRepository = RunTrackerApplication.runRepository

    val allRuns: LiveData<List<Run>>
    val lastRuns: LiveData<List<Run>>

    init {
        allRuns = repository.getAllRuns()
        val calendar =  Calendar.getInstance()
        calendar.add(Calendar.DATE, -7)
        lastRuns = repository.getRunsBetweenDates(calendar.timeInMillis, Calendar.getInstance().timeInMillis)
    }
}