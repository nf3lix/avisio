package com.avisio.dashboard.usecase.training.activity.card_type_strategy

import android.view.View
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment

class ClozeTextLayoutStrategy(private val fragment: LearnBoxFragment) : CardTypeLayoutStrategy(fragment, CardType.CLOZE_TEXT) {

    override fun onShowCard() {
        fragment.answerInputLayout.visibility = View.GONE
    }

    override fun getUserInputAsAnswer(): CardAnswer {
        return fragment.questionInputLayout.getAnswer()
    }

    override fun onCorrectAnswer() {
        fragment.questionInputLayout.correctClozeText()
    }

    override fun onIncorrectAnswer() {
        fragment.questionInputLayout.correctClozeText()
        fragment.correctAnswerEditText.setText(fragment.questionInputLayout.getQuestion().getStringRepresentation())
    }

}