package com.avisio.dashboard.common.data.model.box

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository

class AvisioBoxViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AvisioBoxRepository = AvisioBoxRepository(application)
    private val boxList: LiveData<List<AvisioBox>> = repository.getBoxList()

    fun getBoxList(): LiveData<List<AvisioBox>> {
        return boxList
    }

    fun insert(box: AvisioBox) {
        repository.insert(box)
    }

    fun deleteBox(box: AvisioBox) {
        repository.deleteBox(box)
    }

}