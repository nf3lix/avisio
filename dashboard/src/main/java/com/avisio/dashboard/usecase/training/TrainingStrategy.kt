package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.usecase.training.activity.TrainStrategyObserver

abstract class TrainingStrategy(private val box: AvisioBox) {

    abstract suspend fun onCardResult(result: QuestionResult)
    abstract suspend fun nextCard(): Card?
    abstract fun hasNextCard(): Boolean

    lateinit var trainStrategyObserver: TrainStrategyObserver

     fun initObserver(observer: TrainStrategyObserver) {
         this.trainStrategyObserver = observer
     }

}