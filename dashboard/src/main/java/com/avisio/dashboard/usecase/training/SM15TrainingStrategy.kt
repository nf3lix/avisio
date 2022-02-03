package com.avisio.dashboard.usecase.training

 import android.app.Application
import com.avisio.dashboard.common.data.model.sm.ForgettingCurveEntity
import com.avisio.dashboard.common.data.model.sm.SMCardItem
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.card.CardRepository
import com.avisio.dashboard.common.persistence.forgetting_curves.ForgettingCurveRepository
import com.avisio.dashboard.common.persistence.sm_card_items.SMCardItemRepository
import com.avisio.dashboard.usecase.training.super_memo.MalformedForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import com.avisio.dashboard.usecase.training.super_memo.model.CardItem
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

class SM15TrainingStrategy(val box: AvisioBox, val application: Application) : TrainingStrategy(box) {

    private lateinit var sm: SuperMemo
    private val cardRepository = CardRepository(application)
    private val forgettingCurveRepository = ForgettingCurveRepository(application)
    private val smCardRepository = SMCardItemRepository(application)

    private var cards = listOf<Card>()
    private var smCardItems = arrayListOf<SMCardItem>()

    private var currentCard: Card? = null
    private var currentCardItem: CardItem? = null

    private var count = 0

    private var tempCurves: ForgettingCurves? = null

    override suspend fun onCardResult(result: QuestionResult) {
        sm.answer(result.grade.toDouble(), currentCardItem!!)
        updateForgettingCurves()
        updateCardItem()
        trainStrategyObserver.resultOptionSelectionProceeded()
    }

    override suspend fun nextCard(): Card? {
        prepareTrainingBeforeFirstCard()
        return getCardFromQueue()
    }

    override fun hasNextCard(): Boolean {
        return sm.queue().nextCard() != null
    }

    private fun updateForgettingCurves() {
        val forgettingCurveEntity = ForgettingCurveEntity(
            repetition = currentCardItem!!.repetition(),
            afIndex = currentCardItem!!.afIndex(),
            curve = sm.forgettingCurves().curves()[max(0, currentCardItem!!.repetition())][currentCardItem!!.afIndex()]
        )
        forgettingCurveRepository.updateCurve(forgettingCurveEntity)
    }

    private suspend fun updateCardItem() {
        val smCardToUpdate = SMCardItem.fromCardItem(currentCardItem!!)
        smCardRepository.update(smCardToUpdate)
    }

    private suspend fun prepareTrainingBeforeFirstCard() {
        if(isFirstCardInTraining()) {
            fetchForgettingCurves()
        }

        if(!forgettingCurvesArePersistent()) {
            forgettingCurveRepository.createCurves(sm.forgettingCurves())
        }
    }

    private suspend fun fetchCards() {
        cards = cardRepository.getCardsByBox(box.id)
        for(card in cards) {
            addCardToQueue(card)
        }
    }

    private suspend fun fetchForgettingCurves() {
        var forgettingCurvesLoadSuccess = true
        withContext(Dispatchers.Default) {
            try {
                tempCurves = forgettingCurveRepository.getForgettingCurves()
            } catch (e: MalformedForgettingCurves) {
                forgettingCurvesLoadSuccess = false
            }
        }
        sm = if(!forgettingCurvesLoadSuccess) {
            startForgettingCurvesRecoveryRoutine()
            SuperMemo()
        } else {
            SuperMemo(tempCurves!!)
        }
        fetchCards()
    }

    private fun startForgettingCurvesRecoveryRoutine() {
        forgettingCurveRepository.deleteAll()
    }

    private fun getCardFromQueue(): Card? {
        count++
        val item = sm.queue().nextCard()
        currentCardItem = item
        if(item !== null) {
            currentCard = findCardById(item.cardId)
        }
        return currentCard
    }

    private suspend fun addCardToQueue(card: Card) {
        var smCardItem = smCardRepository.getSMCardItem(card.id)
        if(smCardItem == null) {
            smCardRepository.insert(SMCardItem(cardId = card.id))
            smCardItem = smCardRepository.getSMCardItem(card.id)
        }
        smCardItems.add(smCardItem!!)
        sm.queue().addCard(CardItem.fromSMCardItem(sm, smCardItem))
    }

    private fun forgettingCurvesArePersistent(): Boolean {
        return !forgettingCurveRepository.isEmpty()
    }

    private fun isFirstCardInTraining(): Boolean {
        return count == 0
    }

    private fun findCardById(id: Long): Card? {
        for(item in cards) {
            if(item.id == id) {
                return item
            }
        }
        return null
    }

}