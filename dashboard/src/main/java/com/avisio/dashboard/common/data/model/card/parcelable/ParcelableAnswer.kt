package com.avisio.dashboard.common.data.model.card.parcelable

import android.os.Parcel
import android.os.Parcelable

data class ParcelableAnswer(
    val answerList: ArrayList<String> = ArrayList()
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeStringList(answerList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableAnswer> {
        override fun createFromParcel(parcel: Parcel): ParcelableAnswer {
            return ParcelableAnswer(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableAnswer?> {
            return arrayOfNulls(size)
        }
    }
}