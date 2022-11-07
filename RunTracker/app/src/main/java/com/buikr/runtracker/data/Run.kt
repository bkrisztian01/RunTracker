package com.buikr.runtracker.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Run(
    var id: Int,
    var title: String,
    var date: Date,
    var description: String,
    var duration: Int,          // in seconds
    var distance: Double,       // in kilometers
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