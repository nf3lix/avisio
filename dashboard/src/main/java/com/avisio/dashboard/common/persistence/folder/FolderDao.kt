package com.avisio.dashboard.common.persistence.folder

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

@Dao
interface FolderDao {

    @Query("SELECT * FROM folder")
    fun getAll(): LiveData<List<AvisioFolder>>

    @Query("SELECT * FROM folder WHERE id = :folderId")
    fun getFolderById(folderId: Long): AvisioFolder

    @Insert
    fun insertFolder(folder: AvisioFolder)

    @Query("UPDATE folder SET parentFolder = :destinationFolder WHERE id = :folderToMove")
    fun moveFolder(folderToMove: Long, destinationFolder: Long)

    @Query("UPDATE folder SET parentFolder = null WHERE id = :folderToMove")
    fun moveToRootFolder(folderToMove: Long)

    @Delete
    fun deleteFolder(folder: AvisioFolder)

    @Query("DELETE FROM folder WHERE id = :folderId")
    fun deleteFolder(folderId: Long)

    @Query("SELECT id, parentFolder, 0 AS type, folderName AS name, -1 AS icon, 0 AS selected FROM folder UNION ALL SELECT id, folderId AS parentFolder, 1 AS type, name, icon, 0 AS selected FROM box")
    fun getAllDashboardItems(): LiveData<List<DashboardItem>>

    @Query("SELECT id, parentFolder, 0 AS type, folderName AS name, -1 AS icon, 0 AS selected FROM folder WHERE parentFolder=:id UNION ALL SELECT id, folderId AS parentFolder, 1 AS type, name, icon, 0 AS selected FROM box WHERE folderId=:id")
    fun getAllFromParentId(id: Long): LiveData<List<DashboardItem>>

    @Query("SELECT id, parentFolder, 0 AS type, folderName AS name, -1 AS icon, 0 AS selected FROM folder WHERE parentFolder IS NULL UNION ALL SELECT id, folderId AS parentFolder, 1 AS type, name, icon, 0 AS selected FROM box WHERE folderId IS NULL")
    fun getAllFromRoot(): LiveData<List<DashboardItem>>

    @Query("DELETE FROM folder")
    fun deleteAll()

    @Query("UPDATE folder SET folderName = :folderName WHERE id = :folderId")
    fun updateFolderName(folderId: Long, folderName: String)

}