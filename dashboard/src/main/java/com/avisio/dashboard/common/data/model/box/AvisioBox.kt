package com.avisio.dashboard.common.data.model.box

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
    val name: String = "",

    @ColumnInfo(name = "create_date")
    val createDate: Date = Date(System.currentTimeMillis()),

    @ColumnInfo(name = "icon")
    val icon: BoxIcon = BoxIcon.DEFAULT

) {

    constructor(): this(
        id = 0,
        name = "",
        createDate = Date(System.currentTimeMillis()),
        icon = BoxIcon.DEFAULT
    )

}
