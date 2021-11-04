package com.avisio.dashboard.common.data.model.box

import android.os.Parcel
import android.os.Parcelable
import com.avisio.dashboard.common.ui.edit_box.BoxIcon

data class ParcelableAvisioBox(
    val boxId: Long = 0,
    val boxName: String = "",
    val boxIconId: Int
): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(boxId)
        writeString(boxName)
        writeInt(boxIconId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableAvisioBox> {
        fun createFromEntity(avisioBox: AvisioBox): ParcelableAvisioBox {
            return ParcelableAvisioBox(avisioBox.id, avisioBox.name, avisioBox.icon.iconId)
        }

        fun createEntity(parcelableAvisioBox: ParcelableAvisioBox): AvisioBox {
            return AvisioBox(
                id = parcelableAvisioBox.boxId,
                name = parcelableAvisioBox.boxName,
                icon = BoxIcon.getBoxIcon(parcelableAvisioBox.boxIconId)
            )
        }

        override fun createFromParcel(parcel: Parcel): ParcelableAvisioBox {
            return ParcelableAvisioBox(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableAvisioBox?> {
            return arrayOfNulls(size)
        }
    }

}