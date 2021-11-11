package com.avisio.dashboard.usecase.training.activity.card_type_strategy

import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment

class LearnStandardCardStrategy(val fragment: LearnBoxFragment) : LearnCardTypeStrategy(fragment, CardType.STANDARD) {

    override fun onShowCard() {

    }

    override fun getUserInputAsAnswer(): CardAnswer {
        return CardAnswer(arrayListOf(fragment.answerInputLayout.editText?.text.toString()))
    }

    override fun onCorrectAnswer() {

    }

    override fun onIncorrectAnswer() {

    }
}