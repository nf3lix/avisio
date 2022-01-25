package com.avisio.dashboard.usecase.training.activity

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.training.QuestionResult
import com.avisio.dashboard.usecase.training.TrainingStrategy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LearnCardManager(private val view: LearnCardView, private val strategy: TrainingStrategy) : TrainStrategyObserver {

    private lateinit var currentCard: Card

    init {
        startTraining()
    }

    private fun startTraining() {
        loadNextCard()
    }

    private fun loadNextCard() {
        // view.onCardLoading()
        GlobalScope.launch {
            val card = strategy.nextCard()
            if(card == null) {
                view.onTrainingFinished()
            } else {
                currentCard = card
                view.onCardLoadSuccess(currentCard)
            }
        }
    }

    fun onAnswer(result: QuestionResult) {
        when(result) {
            QuestionResult.EASY -> {
                view.onCorrectAnswer()
            }
            QuestionResult.WRONG -> {
                view.onIncorrectAnswer()
            }
            else -> {
                view.onPartiallyCorrectAnswer()
            }
        }
    }

    fun onResultOptionSelected(result: QuestionResult) {
        GlobalScope.launch {
            strategy.onCardResult(result)
        }
    }

    override fun resultOptionSelectionProceeded() {
        if(strategy.hasNextCard()) {
            loadNextCard()
        } else {
            view.onTrainingFinished()
        }
    }

}