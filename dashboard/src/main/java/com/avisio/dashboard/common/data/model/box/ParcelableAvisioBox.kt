package com.avisio.dashboard.common.data.model.box

import android.os.Parcel
import android.os.Parcelable
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import java.util.*

data class ParcelableAvisioBox(
    val boxId: Long = 0,
    val boxName: String = "",
    val boxIconId: Int,
    val createDate: Long = 0
): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(boxId)
        writeString(boxName)
        writeInt(boxIconId)
        writeLong(createDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableAvisioBox> {
        fun createFromEntity(avisioBox: AvisioBox): ParcelableAvisioBox {
            return ParcelableAvisioBox(avisioBox.id, avisioBox.name, avisioBox.icon.iconId, avisioBox.createDate.time)
        }

        fun createEntity(parcelableAvisioBox: ParcelableAvisioBox): AvisioBox {
            return AvisioBox(
                id = parcelableAvisioBox.boxId,
                name = parcelableAvisioBox.boxName,
                icon = BoxIcon.getBoxIcon(parcelableAvisioBox.boxIconId),
                createDate = Date(parcelableAvisioBox.createDate)
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