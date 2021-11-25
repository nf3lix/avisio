package com.avisio.dashboard.common.persistence

import android.app.Application
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AvisioBoxRepository(application: Application) {

    private val dao: AvisioBoxDao
    private val database: AppDatabase = AppDatabase(application)
    private val boxList: LiveData<List<AvisioBox>>

    init {
        dao = database.boxDao()
        boxList = dao.getBoxList()
    }

    fun getBoxList(): LiveData<List<AvisioBox>> {
        return boxList
    }

    fun insert(box: AvisioBox) {
        GlobalScope.launch {
            dao.insertBox(box)
        }
    }

    fun deleteBox(box: AvisioBox) {
        GlobalScope.launch {
            dao.deleteBox(box)
        }
    }

    fun updateBox(box: AvisioBox) {
        GlobalScope.launch {
            dao.updateBox(box.id, box.name, box.icon.iconId)
        }
    }

    suspend fun getBoxNameList(): List<String> {
        return dao.getBoxNameList()
    }

}