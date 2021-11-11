package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import com.avisio.dashboard.common.data.model.card.CardType

interface CardTypeChangeListener {

    fun onCardTypeSet(cardType: CardType)

}