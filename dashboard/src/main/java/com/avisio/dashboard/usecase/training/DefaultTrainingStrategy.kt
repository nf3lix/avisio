package com.avisio.dashboard.usecase.training

import android.app.Application
import com.avisio.dashboard.common.controller.State
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DefaultTrainingStrategy(val box: AvisioBox, val application: Application) : TrainingStrategy(box) {

    private val cardRepository = CardRepository(application)

    override fun onCardResult(result: QuestionResult) {
        TODO("Not yet implemented")
    }

    override fun nextCard() = flow {
        emit(State.loading(Card()))
        val cardList = cardRepository.getCardsByBox(box.id)
        emit(State.success(cardList[0]))
    }.catch {
        emit(State.failed(it.message.toString()))
    }

    // override fun nextCard() = flow {
    //     emit(State.loading(Card()))
    //     val cardList = cardRepository.getCardsLiveDataByBoxId(box.id)
    //     val card = cardList.value!![(Math.random() * cardList.value!!.size + 1).toInt()]
    //     Log.d("test12345, box_size", box.id.toString())
    //     Log.d("test12345, card", card.question.getStringRepresentation())
    //     emit(State.success(card))
    // }.catch {
    //     emit(State.failed(it.message.toString()))
    // }.flowOn(Dispatchers.IO)

    override fun hasNextCard(): Boolean {
        return true
    }

}