package com.avisio.dashboard.common.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.avisio.dashboard.common.ui.BoxIcon
import java.util.*

@Entity(tableName = "box")
data class AvisioBox(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "create_date")
    val createDate: Date,

    @ColumnInfo(name = "icon")
    val icon: BoxIcon

) {

    constructor(): this(
        id = 0,
        name = "",
        createDate = Date(System.currentTimeMillis()),
        icon = BoxIcon.DEFAULT
    )

}
