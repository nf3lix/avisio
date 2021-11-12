package com.avisio.dashboard.common.ui.edit_card.save_constraints.type

import com.avisio.dashboard.R.string
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.Priority.MEDIUM
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.ANSWER_INPUT
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.QUESTION_INPUT

class SaveClozeTextCardConstraint {

    companion object {

        fun getConstraints(): List<SaveCardConstraint> {
            return listOf(
                clozeTextHasQuestion,
                clozeTextHasActualText,
                separateAnswerIsEmpty
            )
        }

        private val clozeTextHasQuestion = object : SaveCardConstraint(string.create_card_cloze_needs_at_least_one_question, QUESTION_INPUT, MEDIUM) {
            override fun isFulfilled(card: Card): Boolean {
                return card.question.hasQuestionToken()
            }
        }

        private val clozeTextHasActualText = object : SaveCardConstraint(string.edit_card_cloze_text_is_required, QUESTION_INPUT, MEDIUM) {
            override fun isFulfilled(card: Card): Boolean {
                for(token in card.question.tokenList) {
                    if(token.tokenType == CardQuestionTokenType.TEXT && token.content.trim().isNotEmpty()) {
                        return true
                    }
                }
                return false
            }
        }

        private val separateAnswerIsEmpty = object : SaveCardConstraint(string.edit_card_cloze_text_is_required, ANSWER_INPUT, MEDIUM) {
            override fun isFulfilled(card: Card): Boolean {
                return card.answer.answerIsEmpty()
            }
        }

    }

}
