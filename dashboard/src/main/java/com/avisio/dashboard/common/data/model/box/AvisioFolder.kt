package com.avisio.dashboard.common.data.model.box

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "folder",
    foreignKeys = [ForeignKey(entity = AvisioFolder::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parentFolder"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AvisioFolder(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "parentFolder")
    val parentFolder: Long? = null

)