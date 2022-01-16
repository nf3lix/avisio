package com.avisio.dashboard.usecase.training

import android.app.Application
import android.util.Log
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.persistence.ForgettingCurveRepository
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import kotlinx.coroutines.*

class SM15TrainingStrategy(val box: AvisioBox, val application: Application) : TrainingStrategy(box) {

    private lateinit var sm: SuperMemo
    private val cardRepository = CardRepository(application)
    private val forgettingCurveRepository = ForgettingCurveRepository(application)
    private var cards = listOf<Card>()

    private var count = 0

    private var tempCurves: ForgettingCurves? = null

    override fun onCardResult(result: QuestionResult) {
        // persist card result
    }

    override suspend fun nextCard(): Card? {
        if(count == 0) {
            GlobalScope.async(Dispatchers.Default) {
                tempCurves = forgettingCurveRepository.getForgettingCurves()
            }.await()
            sm = SuperMemo(tempCurves!!)
        } else {
            if(tempCurves!!.complyInitialCurves()) {
                forgettingCurveRepository.createCurves(sm.forgettingCurves())
            }
        }
        val cardList = cardRepository.getCardsByBox(box.id)

        count++
        if(cardList.isEmpty()) {
            return null
        }
        return cardList[(Math.random() * cardList.size).toInt()]
    }

    override fun hasNextCard(): Boolean {
        return true
    }

}