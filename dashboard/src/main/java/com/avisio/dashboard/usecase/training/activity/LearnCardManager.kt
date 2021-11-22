package com.avisio.dashboard.usecase.training.activity

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.training.QuestionResult
import com.avisio.dashboard.usecase.training.TrainingStrategy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LearnCardManager(private val view: LearnCardView, private val strategy: TrainingStrategy) {

    private lateinit var currentCard: Card

    init {
        startTraining()
    }

    private fun startTraining() {
        loadNextCard()
    }

    private fun loadNextCard() {
        GlobalScope.launch {
            val card = strategy.nextCard()
            if(card == null) {
                view.onTrainingFinished()
            } else {
                currentCard = card
                view.showCard(currentCard)
            }
        }
    }

    fun onAnswer(result: QuestionResult) {
        when(result) {
            QuestionResult.CORRECT -> {
                view.onCorrectAnswer()
            }
            QuestionResult.INCORRECT -> {
                view.onIncorrectAnswer()
            }
            QuestionResult.PARTIALLY_CORRECT -> {
                view.onPartiallyCorrectAnswer()
            }
        }
    }

    fun onResultOptionSelected(result: QuestionResult) {
        strategy.onCardResult(result)
        if(strategy.hasNextCard()) {
            loadNextCard()
            return
        }
        view.onTrainingFinished()
    }

}