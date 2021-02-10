package com.leonovalexandr.lab7

import android.location.Location
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

// Точка пользователя.
class Point(): Parcelable {

    // Название.
    var name: String = ""

    // Локация.
    var location: Location? = null

    // Дистанция по локации.
    var isNear: Boolean = false

    // Конструктор.
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this() {
        name = parcel.readString() ?: ""
        isNear = parcel.readBoolean()
        location = Location.CREATOR.createFromParcel(parcel)
    }

    // inheriting
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeBoolean(isNear)
        location?.writeToParcel(parcel, flags)
    }

    // inheriting
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Point> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Point {
            return Point(parcel)
        }

        override fun newArray(size: Int): Array<Point?> {
            return arrayOfNulls(size)
        }
    }
}