package com.avisio.dashboard.common.ui.edit_card

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.CreateCardStrategy
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.EditCardFragmentStrategy
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.EditCardStrategy

enum class EditCardFragmentMode {

    CREATE_CARD {
        override fun getFragmentStrategy(fragment: EditCardFragment, card: Card, repository: CardRepository): EditCardFragmentStrategy {
            return CreateCardStrategy(fragment, card, repository)
        }
    },

    EDIT_CARD {
        override fun getFragmentStrategy(fragment: EditCardFragment, card: Card, repository: CardRepository): EditCardFragmentStrategy {
            return EditCardStrategy(fragment, card, repository)
        }
    };

    abstract fun getFragmentStrategy(
        fragment: EditCardFragment,
        card: Card, repository:
        CardRepository): EditCardFragmentStrategy
}