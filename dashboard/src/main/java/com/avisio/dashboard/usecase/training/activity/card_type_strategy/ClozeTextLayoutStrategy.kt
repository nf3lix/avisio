package com.avisio.dashboard.usecase.training.activity.card_type_strategy

import android.util.Log
import android.view.View
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment

class ClozeTextLayoutStrategy(private val fragment: LearnBoxFragment) : CardTypeLayoutStrategy(
    CardType.CLOZE_TEXT
) {

    override fun onShowCard() {
        fragment.answerInputLayout.visibility = View.GONE
    }

    override fun resetCard() {
        fragment.questionInputLayout.setQuestion(CardQuestion.getFromStringRepresentation(""))
    }

    override fun getUserInputAsAnswer(): CardAnswer {
        return fragment.questionInputLayout.getAnswer()
    }

    override fun onCorrectAnswer() {
        fragment.questionInputLayout.correctClozeText()
        fragment.correctAnswerInput.setAnswer(CardAnswer(arrayListOf(fragment.questionInputLayout.getQuestion().getStringRepresentation())))

    }

    override fun onIncorrectAnswer() {
        fragment.questionInputLayout.correctClozeText()
        fragment.correctAnswerInput.setAnswer(CardAnswer(arrayListOf(fragment.questionInputLayout.getQuestion().getStringRepresentation())))

    }

    override fun onPartiallyCorrectAnswer() {
    }

}