package com.buikr.runtracker.data

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Run(
    var id: Long,
    var title: String,
    var date: Date,
    var description: String,
    var duration: Int,          // in seconds
    var distance: Double,       // in kilometers
    var locationData: List<LatLng>
) { }