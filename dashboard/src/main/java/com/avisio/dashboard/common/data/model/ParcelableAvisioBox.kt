package com.avisio.dashboard.common.data.model

import android.os.Parcel
import android.os.Parcelable

data class ParcelableAvisioBox(
    val boxId: Long = 0,
    val boxName: String = ""
): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readLong(),
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(boxId)
        writeString(boxName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableAvisioBox> {
        fun createFromEntity(avisioBox: AvisioBox): ParcelableAvisioBox {
            return ParcelableAvisioBox(avisioBox.id, avisioBox.name)
        }

        override fun createFromParcel(parcel: Parcel): ParcelableAvisioBox {
            return ParcelableAvisioBox(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableAvisioBox?> {
            return arrayOfNulls(size)
        }
    }

}