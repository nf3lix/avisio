package com.avisio.dashboard.usecase.training.activity

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.training.QuestionResult

interface LearnCardView {

    fun onCorrectAnswer()
    fun onIncorrectAnswer()
    fun onPartiallyCorrectAnswer()

    fun onCardLoadSuccess(card: Card)
    fun onCardLoading()
    fun onCardLoadFailure(message: String)

    fun onResultOptionSelected(result: QuestionResult)

    fun onTrainingFinished()

}