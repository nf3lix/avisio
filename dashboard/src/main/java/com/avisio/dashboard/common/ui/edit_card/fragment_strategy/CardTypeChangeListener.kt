package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.CardInputFlexBox

interface CardTypeChangeListener {

    fun onCardTypeSet(cardType: CardType)
    fun onFlexboxInputChanged(input: CardInputFlexBox)

}