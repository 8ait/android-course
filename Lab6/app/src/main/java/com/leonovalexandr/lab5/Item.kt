package com.leonovalexandr.lab5

import android.os.Parcel
import android.os.Parcelable

// Товар.
class Item() : Parcelable {

    // Идентфиикатор.
    var id: Int = 0

    // Тип.
    var kind: String = ""

    // Название.
    var title: String = ""

    // Стоимость.
    var price: Double = 0.0

    // Вес.
    var weight: Double = 0.0

    // Путь для фото.
    var photo: String = ""

    val priceInfo: String
        get() = "$price ₽"

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        kind = parcel.readString() ?: ""
        title = parcel.readString() ?: ""
        photo = parcel.readString() ?: ""
        price = parcel.readDouble()
        weight = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(kind)
        parcel.writeString(title)
        parcel.writeString(photo)
        parcel.writeDouble(price)
        parcel.writeDouble(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}