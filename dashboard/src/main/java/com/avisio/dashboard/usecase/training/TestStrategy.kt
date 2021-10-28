package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card

class DefaultTrainingStrategy(val box: AvisioBox) : TrainingStrategy<DefaultTrainingStrategy.DefaultStrategyQuestionResults>(box) {

    override fun onCardResult(result: DefaultStrategyQuestionResults) {
    }

    override fun nextCard(): Card {
        return Card()
    }

    override fun hasNextCard(): Boolean {
        return true
    }

    enum class DefaultStrategyQuestionResults : QuestionResult {
        CORRECT,
        INCORRECT,
        PARTIALLY_CORRECT
    }


}