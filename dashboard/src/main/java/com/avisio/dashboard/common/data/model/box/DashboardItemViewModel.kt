package com.avisio.dashboard.common.data.model.box

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.common.persistence.folder.AvisioFolderRepository
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class DashboardItemViewModel(application: Application) : AndroidViewModel(application) {

    private val boxRepository: AvisioBoxRepository = AvisioBoxRepository(application)
    private val folderRepository: AvisioFolderRepository = AvisioFolderRepository(application)
    private val boxList: LiveData<List<AvisioBox>> = boxRepository.getBoxList()
    private val dashboardItemList: LiveData<List<DashboardItem>> = boxRepository.getDashboardItemList()

    fun getDashboardItemList(): LiveData<List<DashboardItem>> {
        return dashboardItemList
    }

    fun getBoxList(): LiveData<List<AvisioBox>> {
        return boxList
    }

    fun insert(box: AvisioBox) {
        boxRepository.insert(box)
    }

    fun deleteBox(box: AvisioBox) {
        boxRepository.deleteBox(box)
    }

    fun deleteBox(item: DashboardItem) {
        boxRepository.deleteBox(item.id)
    }

    fun deleteFolder(item: DashboardItem) {
        folderRepository.deleteFolder(AvisioFolder(id = item.id))
    }

}