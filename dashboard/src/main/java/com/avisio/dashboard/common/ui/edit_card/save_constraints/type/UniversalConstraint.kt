package com.avisio.dashboard.common.ui.edit_card.save_constraints.type

import android.text.TextUtils
import com.avisio.dashboard.R.string
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.Priority.HIGH
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.TargetInput.QUESTION_INPUT

sealed class UniversalConstraint(notFulFilledMessageId: Int, target: TargetInput, priority: Priority)
    : SaveCardConstraint(notFulFilledMessageId, target, priority) {

    object QuestionNotEmpty : UniversalConstraint(string.create_card_empty_question, QUESTION_INPUT, HIGH) {
        override fun isFulfilled(card: Card): Boolean {
            return !TextUtils.isEmpty(card.question.getStringRepresentation())
        }
    }

    companion object {
        fun getConstraints(): List<UniversalConstraint> {
            return UniversalConstraint::class.sealedSubclasses
                .map { it.objectInstance as UniversalConstraint }
        }
    }

}
