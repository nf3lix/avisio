package com.avisio.dashboard.usecase.training.activity

import com.avisio.dashboard.common.controller.State
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.training.QuestionResult
import com.avisio.dashboard.usecase.training.TrainingStrategy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LearnCardManager(private val view: LearnCardView, private val strategy: TrainingStrategy) {

    private lateinit var currentCard: Card

    init {
        startTraining()
    }

    private fun startTraining() {
        GlobalScope.launch {
            loadNextCard()
        }
    }

    private suspend fun loadNextCard() {
        strategy.nextCard().collect { state ->
            when(state) {
                is State.Loading -> {
                    view.onCardLoading()
                }
                is State.Failure -> {
                    view.onCardLoadFailure(state.message)
                }
                is State.Success -> {
                    currentCard = state.data
                    view.showCard(currentCard)
                }
            }
        }
    }

    fun onAnswer(answer: String) {
        when(strategy.getQuestionResult(currentCard.question, answer)) {
            QuestionResult.CORRECT -> {
                view.onCorrectAnswer()
            }
            QuestionResult.INCORRECT -> {
                view.onIncorrectAnswer()
            }
            QuestionResult.PARTIALLY_CORRECT -> { }
        }
    }

    fun onResultOptionSelected(result: QuestionResult) {
        strategy.onCardResult(result)
        if(strategy.hasNextCard()) {
            GlobalScope.launch {
                loadNextCard()
            }
            return
        }
        view.onTrainingFinished()
    }

}