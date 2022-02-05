package com.avisio.dashboard.common.data.model.box

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class DashboardItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AvisioBoxRepository = AvisioBoxRepository(application)
    private val boxList: LiveData<List<AvisioBox>> = repository.getBoxList()
    private val dashboardItemList: LiveData<List<DashboardItem>> = repository.getDashboardItemList()

    fun getDashboardItemList(): LiveData<List<DashboardItem>> {
        return dashboardItemList
    }

    fun getBoxList(): LiveData<List<AvisioBox>> {
        return boxList
    }

    fun insert(box: AvisioBox) {
        repository.insert(box)
    }

    fun deleteBox(box: AvisioBox) {
        repository.deleteBox(box)
    }

    fun deleteDashboardItem(item: DashboardItem) {
        repository
    }

}