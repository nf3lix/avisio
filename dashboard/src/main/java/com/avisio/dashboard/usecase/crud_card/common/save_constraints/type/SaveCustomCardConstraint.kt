package com.avisio.dashboard.usecase.crud_card.common.save_constraints.type

import com.avisio.dashboard.R.string
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.Priority.HIGH
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.TargetInput.ANSWER_INPUT

class SaveCustomCardConstraint {

    companion object {

        fun getConstraints(): List<SaveCardConstraint> {
            return listOf(
                answerIsNotEmpty
            )
        }

        private val answerIsNotEmpty = object : SaveCardConstraint(string.create_card_empty_answer, ANSWER_INPUT, HIGH) {
            override fun isFulfilled(card: Card): Boolean {
                return !card.answer.answerIsEmpty()
            }
        }

    }

}
