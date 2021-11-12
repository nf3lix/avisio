package com.avisio.dashboard.common.ui.edit_card.save_constraints.type

import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.Priority.HIGH
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.ANSWER_INPUT

class SaveStandardCardConstraint {

    companion object {

        fun getConstraints(): List<SaveCardConstraint> {
            return listOf(
                answerIsNotEmpty
            )
        }

        private val answerIsNotEmpty = object : SaveCardConstraint(R.string.create_card_empty_answer, ANSWER_INPUT, HIGH) {
            override fun isFulfilled(card: Card): Boolean {
                return !card.answer.answerIsEmpty()
            }
        }

    }

}
