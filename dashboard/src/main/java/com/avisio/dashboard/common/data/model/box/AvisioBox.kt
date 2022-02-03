package com.avisio.dashboard.common.data.model.box

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import java.util.*

@Entity(
    tableName = "box",
    foreignKeys = [ForeignKey(entity = AvisioFolder::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("folderId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AvisioBox(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "folderId")
    val parentFolder: Long? = null,

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
