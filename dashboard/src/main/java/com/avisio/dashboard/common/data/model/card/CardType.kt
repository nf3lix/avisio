package com.avisio.dashboard.common.data.model.card

import android.text.TextUtils
import android.util.Log
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.edit_card.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.EditCardFragmentStrategy

enum class CardType(val iconId: Int) {

    STANDARD(R.drawable.card_icon_standard) {
        override fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {
            val constraintList = arrayListOf(answerNotEmptyConstraint(strategy))
            constraintList.addAll(getUniversalConstraints(strategy))
            return constraintList
        }
    },

    CLOZE_TEXT(R.drawable.card_icon_cloze_text) {
        override fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {

            val clozeTextHasQuestion = object : SaveCardConstraint
                (R.string.create_card_cloze_needs_at_least_one_question,
                strategy.questionFlexBox,
                Priority.MEDIUM
            ) {
                override fun isFulfilled(card: Card): Boolean {
                    return card.question.hasQuestionToken()
                }
            }

            val constraintList = arrayListOf<SaveCardConstraint>(clozeTextHasQuestion)
            constraintList.addAll(getUniversalConstraints(strategy))
            return constraintList
        }
    },

    CUSTOM(R.drawable.card_icon_custom) {
        override fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {
            val constraintList = arrayListOf(answerNotEmptyConstraint(strategy))
            constraintList.addAll(getUniversalConstraints(strategy))
            return constraintList
        }
    };

    abstract fun getSaveCardConstraints(strategy: EditCardFragmentStrategy): List<SaveCardConstraint>

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
                    Log.d("test1234", card.answer.getStringRepresentation())
                    return !card.answer.answerIsEmpty()
                }
            }
            return constraint
        }

    }

}