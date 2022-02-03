package com.avisio.dashboard.common.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.avisio.dashboard.common.data.model.box.AvisioFolder

@Dao
interface FolderDao {

    @Query("SELECT * FROM folder")
    fun getAll(): LiveData<List<AvisioFolder>>

    @Insert
    fun insertFolder(folder: AvisioFolder)

    @Query("UPDATE folder SET parentFolder = :destinationFolder WHERE id = :folderToMove")
    fun moveFolder(folderToMove: Long, destinationFolder: Long)

    @Delete
    fun deleteFolder(folder: AvisioFolder)

}