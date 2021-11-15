package com.avisio.dashboard.usecase.training.activity.card_type_strategy

import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment

class StandardCardLayoutStrategy(val fragment: LearnBoxFragment) : CardTypeLayoutStrategy(CardType.STRICT) {

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

    }

    override fun onIncorrectAnswer() {

    }
}