package com.avisio.dashboard.common.data.model.card

import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.usecase.training.QuestionResult

enum class CardType(val iconId: Int) {

    STANDARD(R.drawable.card_icon_standard) {
        override fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult {
            return when(cardQuestion.getStringRepresentation() == cardAnswer.getStringRepresentation()) {
                true -> QuestionResult.CORRECT
                false -> QuestionResult.INCORRECT
            }
        }
    },

    CLOZE_TEXT(R.drawable.card_icon_cloze_text) {
        override fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult {
            val answerTokenList = cardAnswer.answerList
            val questionTokenList = arrayListOf<CardQuestionToken>()
            for(questionToken in cardQuestion.tokenList) {
                if(questionToken.tokenType == CardQuestionTokenType.QUESTION) {
                    questionTokenList.add(questionToken)
                }
            }
            if(answerTokenList.size != questionTokenList.size) {
                return QuestionResult.INCORRECT
            }
            for((index, questionToken) in questionTokenList.withIndex()) {
                if(questionToken.content != answerTokenList[index]) {
                    return QuestionResult.INCORRECT
                }
            }
            return QuestionResult.CORRECT
        }
    },

    CUSTOM(R.drawable.card_icon_custom) {
        override fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult {
            return QuestionResult.PARTIALLY_CORRECT
        }
    };

    abstract fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult

}