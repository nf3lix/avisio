package com.avisio.dashboard.common.ui.edit_card.save_constraints.type

import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.Priority.*
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.*

sealed class SaveStandardCardConstraint(notFulFilledMessageId: Int, target: TargetInput, priority: Priority)
    : SaveCardConstraint(notFulFilledMessageId, target, priority) {

    object AnswerIsNotEmpty : SaveStandardCardConstraint(R.string.create_card_empty_answer, ANSWER_INPUT, HIGH) {
        override fun isFulfilled(card: Card): Boolean {
            return !card.answer.answerIsEmpty()
        }
    }

    companion object {
        fun getConstraints(): List<SaveCardConstraint> {
            return SaveStandardCardConstraint::class.sealedSubclasses
                .map { it.objectInstance as SaveStandardCardConstraint }
        }
    }

}
