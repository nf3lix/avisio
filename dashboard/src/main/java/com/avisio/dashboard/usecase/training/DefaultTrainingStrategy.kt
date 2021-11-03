package com.avisio.dashboard.usecase.training

import android.app.Application
import com.avisio.dashboard.common.controller.State
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class DefaultTrainingStrategy(val box: AvisioBox, val application: Application) : TrainingStrategy(box) {

    private val cardRepository = CardRepository(application)

    override fun onCardResult(result: QuestionResult) {
        // persist card result
    }

    override fun nextCard() = flow {
        emit(State.loading(Card()))
        val cardList = cardRepository.getCardsByBox(box.id)
        emit(State.success(cardList[(Math.random() * cardList.size).toInt()]))
    }.catch {
        emit(State.failed(it.message.toString()))
    }

    override fun hasNextCard(): Boolean {
        return Random.nextBoolean()
    }

}