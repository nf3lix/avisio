package com.avisio.dashboard.common.ui.edit_card

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.CardInputFlexBox

abstract class SaveCardConstraint(
    val notFulfilledMessageId: Int,
    val targetInput: CardInputFlexBox,
    priority: SaveCardConstraint.Priority) {

    abstract fun isFulfilled(card: Card): Boolean

    enum class Priority {
        LOW, MEDIUM, HIGH
    }

}