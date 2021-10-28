package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card

abstract class TrainingStrategy<T: TrainingStrategy.QuestionResult>(private val box: AvisioBox) {

    abstract fun onCardResult(result: T)
    abstract fun nextCard(): Card
    abstract fun hasNextCard(): Boolean

    interface QuestionResult

}