package com.avisio.dashboard.common.persistence.folder

import android.app.Application
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioFolder
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

}