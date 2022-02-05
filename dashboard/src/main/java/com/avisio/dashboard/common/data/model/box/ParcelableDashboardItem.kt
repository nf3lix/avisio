package com.avisio.dashboard.common.data.model.box

import android.os.Parcel
import android.os.Parcelable
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType

class ParcelableDashboardItem(
    val id: Long = 0,
    val parentFolder: Long?,
    val type: DashboardItemType,
    val name: String,
    val icon: Int
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        DashboardItemType.values()[parcel.readInt()],
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeValue(parentFolder)
        parcel.writeInt(type.ordinal)
        parcel.writeString(name)
        parcel.writeInt(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableDashboardItem> {
        override fun createFromParcel(parcel: Parcel): ParcelableDashboardItem {
            return ParcelableDashboardItem(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableDashboardItem?> {
            return arrayOfNulls(size)
        }

        fun createFromEntity(item: DashboardItem): ParcelableDashboardItem {
            return ParcelableDashboardItem(
                item.id,
                item.parentFolder,
                item.type,
                item.name!!,
                item.icon)
        }

        fun createEntity(item: ParcelableDashboardItem): DashboardItem {
            return DashboardItem(
                item.id,
                item.parentFolder,
                item.type,
                item.name,
                item.icon
            )
        }

    }


}