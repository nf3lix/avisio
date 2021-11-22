package com.avisio.dashboard.usecase.training.activity.card_type_strategy

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.usecase.training.QuestionResult
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment

abstract class CardTypeLayoutStrategy(val cardType: CardType) {

    abstract fun onShowCard()
    abstract fun resetCard()
    abstract fun getUserInputAsAnswer(): CardAnswer
    abstract fun onCorrectAnswer()
    abstract fun onIncorrectAnswer()
    abstract fun onPartiallyCorrectAnswer()

    fun getQuestionResult(card: Card, cardAnswer: CardAnswer): QuestionResult {
        return cardType.getQuestionResult(card, cardAnswer)
    }

    companion object {

        fun getCardTypeStrategy(card: Card, fragment: LearnBoxFragment): CardTypeLayoutStrategy {
            return when(card.type) {
                CardType.STRICT -> StrictCardLayoutStrategy(fragment)
                CardType.CLOZE_TEXT -> ClozeTextLayoutStrategy(fragment)
                CardType.STANDARD -> StandardCardLayoutStrategy(fragment)
            }
        }

    }

}