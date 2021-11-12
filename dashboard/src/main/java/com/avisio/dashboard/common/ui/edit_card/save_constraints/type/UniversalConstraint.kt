package com.avisio.dashboard.common.ui.edit_card.save_constraints.type

import android.text.TextUtils
import com.avisio.dashboard.R.string
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.Priority.HIGH
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.QUESTION_INPUT

class UniversalConstraint {

    companion object {

        fun getConstraints(): List<SaveCardConstraint> {
            return listOf(
                questionNotEmpty
            )
        }

        private val questionNotEmpty = object : SaveCardConstraint(string.create_card_empty_question, QUESTION_INPUT, HIGH) {
            override fun isFulfilled(card: Card): Boolean {
                return !TextUtils.isEmpty(card.question.getStringRepresentation())
            }
        }

    }

}
