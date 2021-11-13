package com.avisio.dashboard.usecase.crud_card.common

import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CreateCardStrategy
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.EditCardFragmentStrategy
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.EditCardStrategy

enum class EditCardFragmentMode(val activityLayout: Int, val activityActionBarTitleId: Int) {

    CREATE_CARD(R.layout.activity_create_card, R.string.create_card_action_bar_title) {
        override fun getFragmentStrategy(fragment: EditCardFragment, card: Card, repository: CardRepository): EditCardFragmentStrategy {
            return CreateCardStrategy(fragment, card, repository)
        }
    },

    EDIT_CARD(R.layout.activity_edit_card, R.string.edit_card_action_bar_title) {
        override fun getFragmentStrategy(fragment: EditCardFragment, card: Card, repository: CardRepository): EditCardFragmentStrategy {
            return EditCardStrategy(fragment, card, repository)
        }
    };

    abstract fun getFragmentStrategy(
        fragment: EditCardFragment,
        card: Card, repository:
        CardRepository): EditCardFragmentStrategy
}