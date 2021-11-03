package com.avisio.dashboard.usecase.training.activity

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.usecase.training.QuestionResult

interface LearnCardView {

    fun onCorrectAnswer()
    fun onIncorrectAnswer()

    fun showCard(card: Card)
    fun onCardLoadFailure(message: String)
    fun onCardLoading()

    fun onResultOptionSelected(result: QuestionResult)

}