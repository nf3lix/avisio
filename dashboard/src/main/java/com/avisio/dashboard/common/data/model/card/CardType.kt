package com.avisio.dashboard.common.data.model.card

import android.text.TextUtils
import android.util.Log
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.ui.edit_card.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.EditCardFragmentStrategy
import com.avisio.dashboard.usecase.training.QuestionResult

enum class CardType(val iconId: Int) {

    STANDARD(R.drawable.card_icon_standard) {
        override fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {
            val constraintList = arrayListOf(answerNotEmptyConstraint(strategy))
            constraintList.addAll(getUniversalConstraints(strategy))
            return constraintList
        }

        override fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult {
            return when(cardQuestion.getStringRepresentation() == cardAnswer.getStringRepresentation()) {
                true -> QuestionResult.CORRECT
                false -> QuestionResult.INCORRECT
            }
        }
    },

    CLOZE_TEXT(R.drawable.card_icon_cloze_text) {
        override fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {

            val clozeTextHasQuestion = object : SaveCardConstraint(R.string.create_card_cloze_needs_at_least_one_question, strategy.questionFlexBox, Priority.MEDIUM) {
                override fun isFulfilled(card: Card): Boolean {
                    return card.question.hasQuestionToken()
                }
            }

            val clozeTextHasActualText = object : SaveCardConstraint(R.string.edit_card_cloze_text_is_required, strategy.questionFlexBox, Priority.MEDIUM) {
                override fun isFulfilled(card: Card): Boolean {
                    for(token in card.question.tokenList) {
                        if(token.tokenType == CardQuestionTokenType.TEXT) {
                            return true
                        }
                    }
                    return false
                }

            }

            val constraintList = arrayListOf(clozeTextHasQuestion, clozeTextHasActualText)
            constraintList.addAll(getUniversalConstraints(strategy))
            return constraintList
        }

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
        override fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {
            val constraintList = arrayListOf(answerNotEmptyConstraint(strategy))
            constraintList.addAll(getUniversalConstraints(strategy))
            return constraintList
        }

        override fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult {
            return QuestionResult.PARTIALLY_CORRECT
        }
    };

    abstract fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint>
    abstract fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult

    fun getUniversalConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {

        val questionNotEmpty = object : SaveCardConstraint(
            R.string.create_card_empty_question,
            strategy.questionFlexBox,
            Priority.HIGH) {
            override fun isFulfilled(card: Card): Boolean {
                return !TextUtils.isEmpty(card.question.getStringRepresentation())
            }
        }

        return listOf(questionNotEmpty)

    }

    companion object {

        private fun answerNotEmptyConstraint(strategy: EditCardFragmentStrategy): SaveCardConstraint {
            val constraint = object : SaveCardConstraint(
                R.string.create_card_empty_answer, strategy.answerFlexBox, Priority.HIGH) {

                override fun isFulfilled(card: Card): Boolean {
                    return !card.answer.answerIsEmpty()
                }
            }
            return constraint
        }

    }

}