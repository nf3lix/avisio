package com.avisio.dashboard.common.persistence.folder

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

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

    @Query("SELECT " + "id, parentFolder, 0 AS type FROM folder WHERE parentFolder=:id " +
        "UNION ALL SELECT id, folderId AS parentFolder, 1 AS type FROM box WHERE folderId=:id")
    fun getAllFromParentId(id: Long): LiveData<List<DashboardItem>>

    @Query("SELECT " + "id, parentFolder, 0 AS type FROM folder WHERE parentFolder IS NULL " +
            "UNION ALL SELECT id, folderId AS parentFolder, 1 AS type FROM box WHERE folderId IS NULL")
    fun getAllFromRoot(): LiveData<List<DashboardItem>>

}