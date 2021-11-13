package com.avisio.dashboard.usecase.crud_card.common.fragment_strategy

import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardInputFlexBox

interface CardTypeChangeListener {

    fun onCardTypeSet(cardType: CardType)
    fun onFlexboxInputChanged(input: CardInputFlexBox)

}