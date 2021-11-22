package com.avisio.dashboard.common.persistence

import android.app.Application
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.card.Card
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardRepository(application: Application) {

    private val dao: CardDao
    private val database: AppDatabase = AppDatabase(application)
    private val cardList: LiveData<List<Card>>

    init {
        dao = database.cardDao()
        cardList = dao.getAllAsLiveData()
    }

    suspend fun getCardsByBox(boxId: Long): List<Card> {
        return dao.getCardsByBox(boxId)
    }

    fun getCardsLiveDataByBoxId(boxId: Long): LiveData<List<Card>> {
        return dao.getCardsLiveDataByBox(boxId)
    }

    fun insertCard(card: Card) {
        GlobalScope.launch {
            dao.insert(card)
        }
    }

    fun deleteCard(card: Card) {
        GlobalScope.launch {
            dao.deleteCard(card)
        }
    }

    fun updateCard(card: Card) {
        GlobalScope.launch {
            dao.updateCard(card)
        }
    }

}