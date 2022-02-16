package com.avisio.dashboard.usecase.crud_card.read

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.card.CardRepository

class CardViewModel(application: Application, val box: AvisioBox) : AndroidViewModel(application) {

    private val repository: CardRepository = CardRepository(application)
    private var cardList: LiveData<List<Card>> = repository.getCardsLiveDataByBoxId(box.id)
    private var cardListWithSMDetails: LiveData<List<CardViewHolderItem>> = repository.getCardsLiveDataByBoxIdWithSMDetails(box.id)

    fun getCardList(): LiveData<List<Card>> {
        return cardList
    }

    fun getCardListWithSMDetails(): LiveData<List<CardViewHolderItem>> {
        return cardListWithSMDetails
    }

    fun insertCard(card: Card) {
        repository.insertCard(card)
    }

    fun deleteCard(card: Card) {
        repository.deleteCard(card)
    }

}