package com.avisio.dashboard.common.ui.edit_card

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.EditCardFragmentStrategy

object SaveCardValidator {

    fun getUnfulfilledConstraints(card: Card, strategy: EditCardFragmentStrategy): List<SaveCardConstraint> {
        val unfulfilled = arrayListOf<SaveCardConstraint>()
        for(constraint in card.type.getSaveCardConstraints(strategy)) {
            if(!constraint.isFulfilled(card)) {
                unfulfilled.add(constraint)
            }
        }
        return unfulfilled
    }

}