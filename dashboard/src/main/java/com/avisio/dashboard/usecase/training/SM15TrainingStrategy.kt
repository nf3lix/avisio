package com.avisio.dashboard.usecase.training

import android.app.Application
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SM15TrainingStrategy(val box: AvisioBox, val application: Application) : TrainingStrategy(box) {

    private val sm = SuperMemo()
    private val cardRepository = CardRepository(application)
    private var cards = listOf<Card>()

    init {
        GlobalScope.launch {
            cards = fetchCards()
            for(card in cards) {
            }
        }
    }

    private suspend fun fetchCards(): List<Card> {
        return cardRepository.getCardsByBox(box.id)
    }

    override fun onCardResult(result: QuestionResult) {
        TODO("Not yet implemented")
    }

    override suspend fun nextCard(): Card? {
        TODO("Not yet implemented")
    }

    override fun hasNextCard(): Boolean {
        TODO("Not yet implemented")
    }

}