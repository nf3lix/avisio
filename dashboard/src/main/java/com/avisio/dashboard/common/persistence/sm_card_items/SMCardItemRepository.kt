package com.avisio.dashboard.common.persistence.sm_card_items

import android.app.Application
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.sm.SMCardItem

class SMCardItemRepository(application: Application) {

    private val dao: SMCardItemDao
    private val database: AppDatabase = AppDatabase(application)

    init {
        dao = database.smCardItemDao()
    }

    suspend fun getSMCardItem(cardId: Long): SMCardItem? {
        return dao.getCardItem(cardId)
    }

    suspend fun insert(smCardItem: SMCardItem) {
        dao.insert(smCardItem)
    }

    suspend fun update(smCardItem: SMCardItem) {
        dao.update(smCardItem)
    }

}