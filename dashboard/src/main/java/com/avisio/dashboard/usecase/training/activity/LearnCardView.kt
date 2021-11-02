package com.avisio.dashboard.usecase.training.activity

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestion

interface LearnCardView {
    fun showCardQuestion(question: CardQuestion)
    fun onCorrectAnswer()
    fun onIncorrectAnswer()
    fun onCardLoadFailure(message: String)
    fun onCardLoading()
    fun onCardLoadSuccess(card: Card)
}