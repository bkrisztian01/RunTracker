package com.buikr.runtracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Run(
    val id: Int,
    val title: String,
    val date: Date,
    val description: String,
    val duration: Int,          // in seconds
    val distance: Double,       // in kilometers
) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readInt(),
//        parcel.readString().toString(),
//        Date(parcel.readLong()),
//        parcel.readString().toString(),
//        parcel.readInt(),
//        parcel.readDouble()
//    ) {
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(id)
//        parcel.writeString(title)
//        parcel.writeLong(date.time)
//        parcel.writeString(description)
//        parcel.writeInt(duration)
//        parcel.writeDouble(distance)
//    }
//
//    companion object CREATOR : Parcelable.Creator<Run> {
//        override fun createFromParcel(parcel: Parcel): Run {
//            return Run(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Run?> {
//            return arrayOfNulls(size)
//        }
//    }
}