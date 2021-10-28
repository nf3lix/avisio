package com.avisio.dashboard.usecase.crud_box.box_activity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox

class CardViewModelFactory(
    private val application: Application,
    private val box: ParcelableAvisioBox
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CardViewModel(application, box) as T
    }

}