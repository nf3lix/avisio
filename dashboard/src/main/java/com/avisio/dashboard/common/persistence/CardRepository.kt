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
        cardList = dao.getAll()
    }

    fun getCardList(): LiveData<List<Card>> {
        return cardList
    }

    fun getCardsByBox(boxId: Int): LiveData<List<Card>> {
        return dao.getCardsByBox(boxId)
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

}