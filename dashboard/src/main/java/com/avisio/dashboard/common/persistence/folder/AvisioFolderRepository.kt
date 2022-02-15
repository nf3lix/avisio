package com.avisio.dashboard.common.persistence.folder

import android.app.Application
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AvisioFolderRepository(application: Application) {

    private val database: AppDatabase = AppDatabase(application)
    private val folderDao: FolderDao = database.folderDao()

    fun createFolder(folder: AvisioFolder) {
        GlobalScope.launch {
            folderDao.insertFolder(folder)
        }
    }

    fun deleteFolder(folder: AvisioFolder) {
        GlobalScope.launch {
            folderDao.deleteFolder(folder.id)
        }
    }

    fun updateFolderName(folder: AvisioFolder) {
        GlobalScope.launch {
            folderDao.updateFolderName(folder.id, folder.name)
        }
    }

    fun moveFolder(folder: AvisioFolder, destination: DashboardItem) {
        GlobalScope.launch {
            if(destination.id == -1L) {
                folderDao.moveToRootFolder(folder.id)
            } else {
                folderDao.moveFolder(folder.id, destination.id)
            }
        }
    }

}