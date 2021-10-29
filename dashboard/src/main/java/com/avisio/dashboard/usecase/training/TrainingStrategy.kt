package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import java.util.*

abstract class TrainingStrategy(private val box: AvisioBox) {

    abstract fun onCardResult(result: QuestionResult)
    abstract fun nextCard(): Card
    abstract fun hasNextCard(): Boolean
    abstract fun getQuestionResults(): LinkedList<QuestionResult>

}