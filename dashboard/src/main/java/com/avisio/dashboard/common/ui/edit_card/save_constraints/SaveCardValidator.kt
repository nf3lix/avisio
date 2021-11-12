package com.avisio.dashboard.common.ui.edit_card.save_constraints

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.save_constraints.SaveCardConstraint.*

object SaveCardValidator {

    fun getUnfulfilledConstraints(card: Card): List<SaveCardConstraint> {
        val constraintList = SaveCardConstraint.getConstraints(card.type)
        return getUnfulfilledList(card, constraintList)
    }

    fun getTargetSpecificUnfulfilledConstraints(card: Card, targetInput: TargetInput): List<SaveCardConstraint> {
        val constraintList = SaveCardConstraint.getInputTargetSpecificConstraints(card.type, targetInput)
        return getUnfulfilledList(card, constraintList)
    }

    private fun getUnfulfilledList(card: Card, constraintList: List<SaveCardConstraint>): List<SaveCardConstraint> {
        val unfulfilled = arrayListOf<SaveCardConstraint>()
        for(constraint in constraintList) {
            if(!constraint.isFulfilled(card)) {
                unfulfilled.add(constraint)
            }
        }
        return unfulfilled
    }

}