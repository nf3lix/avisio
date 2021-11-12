package com.avisio.dashboard.common.ui.edit_card.save_constraints.type

import com.avisio.dashboard.R.string
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.Priority.HIGH
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.ANSWER_INPUT

sealed class SaveCustomCardConstraint(notFulFilledMessageId: Int, target: TargetInput, priority: Priority)
    : SaveCardConstraint(notFulFilledMessageId, target, priority) {

    object AnswerIsNotEmpty : SaveCustomCardConstraint(string.create_card_empty_answer, ANSWER_INPUT, HIGH) {
        override fun isFulfilled(card: Card): Boolean {
            return !card.answer.answerIsEmpty()
        }
    }

    companion object {
        fun getConstraints(): List<SaveCardConstraint> {
            return SaveCustomCardConstraint::class.sealedSubclasses
                .map { it.objectInstance as SaveCustomCardConstraint }
        }
    }

}
