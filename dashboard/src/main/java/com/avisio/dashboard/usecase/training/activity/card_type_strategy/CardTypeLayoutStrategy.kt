package com.avisio.dashboard.usecase.training.activity.card_type_strategy

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.usecase.training.QuestionResult
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment

abstract class CardTypeLayoutStrategy(fragment: LearnBoxFragment, val cardType: CardType) {

    abstract fun onShowCard()
    abstract fun getUserInputAsAnswer(): CardAnswer
    abstract fun onCorrectAnswer()
    abstract fun onIncorrectAnswer()

    fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult {
        return cardType.getQuestionResult(cardQuestion, cardAnswer)
    }

    companion object {

        fun getCardTypeStrategy(card: Card, fragment: LearnBoxFragment): CardTypeLayoutStrategy {
            return when(card.type) {
                CardType.STANDARD -> StandardCardLayoutStrategy(fragment)
                CardType.CLOZE_TEXT -> ClozeTextLayoutStrategy(fragment)
                else -> StandardCardLayoutStrategy(fragment)
            }
        }

    }

}