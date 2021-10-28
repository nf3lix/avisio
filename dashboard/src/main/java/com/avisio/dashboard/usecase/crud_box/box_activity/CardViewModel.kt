package com.avisio.dashboard.usecase.crud_box.box_activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository

class CardViewModel(application: Application, val box: ParcelableAvisioBox) : AndroidViewModel(application) {

    private val repository: CardRepository = CardRepository(application)
    private var cardList: LiveData<List<Card>> = repository.getCardsByBox(box.boxId)

    fun getCardList(): LiveData<List<Card>> {
        return cardList
    }

    fun insertCard(card: Card) {
        repository.insertCard(card)
    }

    fun deleteCard(card: Card) {
        repository.deleteCard(card)
    }

}