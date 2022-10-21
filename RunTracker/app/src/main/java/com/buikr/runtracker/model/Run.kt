package com.buikr.runtracker.model

import java.time.LocalDate

data class Run(
    val id: Int,
    val title: String,
    val date: LocalDate,
    val description: String
) {}