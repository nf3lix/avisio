package com.avisio.dashboard.common.ui.edit_card.save_constraints

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.ui.edit_card.save_constraints.type.SaveClozeTextCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.type.SaveCustomCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.type.SaveStandardCardConstraint
import com.avisio.dashboard.common.ui.edit_card.save_constraints.type.UniversalConstraint

abstract class SaveCardConstraint(
    val notFulfilledMessageId: Int,
    val targetInput: TargetInput,
    priority: Priority
) {

    abstract fun isFulfilled(card: Card): Boolean

    enum class Priority {
        LOW, MEDIUM, HIGH
    }

    enum class TargetInput {
        QUESTION_INPUT, ANSWER_INPUT
    }

    companion object {

        fun getConstraints(cardType: CardType): List<SaveCardConstraint> {
            val typeSpecificConstraints = when(cardType) {
                CardType.STANDARD -> {
                    SaveStandardCardConstraint.getConstraints()
                }
                CardType.CLOZE_TEXT -> {
                    SaveClozeTextCardConstraint.getConstraints()
                }
                CardType.CUSTOM -> {
                    SaveCustomCardConstraint.getConstraints()
                }
            }
            return mergeConstraints(typeSpecificConstraints, UniversalConstraint.getConstraints())
        }

        fun getInputTargetSpecificConstraints(cardType: CardType, targetInput: TargetInput): List<SaveCardConstraint> {
            val unfilteredConstraints = getConstraints(cardType)
            val filtered = arrayListOf<SaveCardConstraint>()
            for(constraint in unfilteredConstraints) {
                if(constraint.targetInput == targetInput) {
                    filtered.add(constraint)
                }
            }
            return filtered
        }

        private fun mergeConstraints(
            typeSpecificConstraints: List<SaveCardConstraint>,
            universalConstraint: List<SaveCardConstraint>): ArrayList<SaveCardConstraint>
        {
            val specificConstraints = ArrayList(typeSpecificConstraints)
            specificConstraints.addAll(universalConstraint)
            return specificConstraints
        }

    }

}