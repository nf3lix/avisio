package com.avisio.dashboard.common.persistence.box

import android.app.Application
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.persistence.folder.FolderDao
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AvisioBoxRepository(application: Application) {

    private val boxDao: AvisioBoxDao
    private val folderDao: FolderDao
    private val database: AppDatabase = AppDatabase(application)
    private val boxList: LiveData<List<AvisioBox>>
    private val dashboardItemList: LiveData<List<DashboardItem>>

    init {
        boxDao = database.boxDao()
        folderDao = database.folderDao()
        boxList = boxDao.getBoxList()
        dashboardItemList = folderDao.getAllDashboardItems()
    }

    fun getBoxList(): LiveData<List<AvisioBox>> {
        return boxList
    }

    fun getDashboardItemList(): LiveData<List<DashboardItem>> {
        return dashboardItemList
    }

    fun insert(box: AvisioBox) {
        GlobalScope.launch {
            boxDao.insertBox(box)
        }
    }

    fun deleteBox(box: AvisioBox) {
        GlobalScope.launch {
            boxDao.deleteBox(box)
        }
    }

    fun deleteBox(boxId: Long) {
        GlobalScope.launch {
            boxDao.deleteBox(boxId)
        }
    }

    fun updateBox(box: AvisioBox) {
        GlobalScope.launch {
            boxDao.updateBox(box.id, box.name, box.icon.iconId)
        }
    }

    fun getBoxById(id: Long): AvisioBox {
        return boxDao.getBoxById(id)
    }

    suspend fun getBoxNameList(): List<String> {
        return boxDao.getBoxNameList()
    }

    fun moveBox(avisioBox: AvisioBox, destination: DashboardItem) {
        GlobalScope.launch {
            if(destination.id == -1L) {
                boxDao.moveToRootFolder(avisioBox.id)
            } else {
                boxDao.moveBox(avisioBox.id, destination.id)
            }
        }
    }

}