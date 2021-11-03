package com.avisio.dashboard.usecase.training

import android.app.Application
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository

class DefaultTrainingStrategy(val box: AvisioBox, val application: Application) : TrainingStrategy(box) {

    private val cardRepository = CardRepository(application)

    override fun onCardResult(result: QuestionResult) {
        // persist card result
    }

    override suspend fun nextCard(): Card {
        val cardList = cardRepository.getCardsByBox(box.id)
        return cardList[(Math.random() * cardList.size).toInt()]
    }

    override fun hasNextCard(): Boolean {
        return false
    }

}