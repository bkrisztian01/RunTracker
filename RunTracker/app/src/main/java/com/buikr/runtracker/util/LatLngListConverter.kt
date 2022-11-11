package com.buikr.runtracker.util

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LatLngListConverter {
    @TypeConverter
    fun fromLatLngList(latLngList: List<LatLng>?): String? {
        return latLngList?.let { _latLngList ->
            val gson = Gson()
            val type = object : TypeToken<List<LatLng>>() {}.type
            return gson.toJson(_latLngList, type)
        }
    }

    @TypeConverter
    fun toLatLngList(json: String?): List<LatLng>? {
        return json?.let { _json ->
            val gson = Gson()
            val type = object : TypeToken<List<LatLng>>() {}.type
            return gson.fromJson(_json, type)
        }
    }
}