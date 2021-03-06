package com.avisio.dashboard.usecase.training.activity.card_type_strategy

import android.view.View
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment
import kotlinx.android.synthetic.main.fragment_learn_box.*

class StrictCardLayoutStrategy(val fragment: LearnBoxFragment) : CardTypeLayoutStrategy(CardType.STRICT) {

    override fun onShowCard() {

    }

    override fun resetCard() {
        fragment.answerEditText.setText("")
        fragment.questionInputLayout.setQuestion(CardQuestion.getFromStringRepresentation(""))
    }

    override fun getUserInputAsAnswer(): CardAnswer {
        return CardAnswer(arrayListOf(fragment.answerInputLayout.editText?.text.toString()))
    }

    override fun onCorrectAnswer() {
        fragment.userAnswerInput.visibility = View.VISIBLE
        fragment.userAnswerInput.setText(fragment.answerInputLayout.editText?.text.toString())

    }

    override fun onIncorrectAnswer() {
        fragment.userAnswerInput.visibility = View.VISIBLE
        fragment.userAnswerInput.setText(fragment.answerInputLayout.editText?.text.toString())

    }

    override fun onPartiallyCorrectAnswer() {
    }
}