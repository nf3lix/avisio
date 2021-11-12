package com.avisio.dashboard.common.ui.edit_card.save_constraints.type

import com.avisio.dashboard.R.string
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.Priority.MEDIUM
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.QUESTION_INPUT

sealed class SaveClozeTextCardConstraint(notFulFilledMessageId: Int, target: TargetInput, priority: Priority)
    : SaveCardConstraint(notFulFilledMessageId, target, priority) {

    companion object {
        fun getConstraints(): List<SaveCardConstraint> {
            return SaveClozeTextCardConstraint::class.sealedSubclasses
                .map { it.objectInstance as SaveClozeTextCardConstraint }
        }
    }

    object ClozeTextHasQuestion : SaveClozeTextCardConstraint(string.create_card_cloze_needs_at_least_one_question, QUESTION_INPUT, MEDIUM) {
        override fun isFulfilled(card: Card): Boolean {
            return card.question.hasQuestionToken()
        }
    }

    object ClozeTextHasActualText : SaveClozeTextCardConstraint(string.edit_card_cloze_text_is_required, QUESTION_INPUT, MEDIUM) {
        override fun isFulfilled(card: Card): Boolean {
            for(token in card.question.tokenList) {
                if(token.tokenType == CardQuestionTokenType.TEXT) {
                    return true
                }
            }
            return false
        }
    }

}
