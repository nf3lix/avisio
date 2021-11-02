package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.common.controller.State
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import kotlinx.coroutines.flow.Flow

abstract class TrainingStrategy(private val box: AvisioBox) {

    abstract fun onCardResult(result: QuestionResult)
    abstract fun nextCard(): Flow<State<Card>>
    abstract fun hasNextCard(): Boolean

    fun getQuestionResult(question: CardQuestion, answer: String): QuestionResult {
        return when(question.getStringRepresentation() == answer) {
            true -> QuestionResult.CORRECT
            false -> QuestionResult.INCORRECT
        }
    }

}