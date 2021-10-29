package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import java.util.*

class DefaultTrainingStrategy(val box: AvisioBox) : TrainingStrategy(box) {

    override fun onCardResult(result: QuestionResult) {
        TODO("Not yet implemented")
    }

    override fun nextCard(): Card {
        return Card()
    }

    override fun hasNextCard(): Boolean {
        return true
    }

    override fun getQuestionResults(): LinkedList<QuestionResult> {
        val list = LinkedList<QuestionResult>()
        list.add(QuestionResult("CORRECT"))
        list.add(QuestionResult("PARTIALLY_CORRECT"))
        list.add(QuestionResult("INCORRECT"))
        return list
    }

}