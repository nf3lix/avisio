package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.CardItem
import com.avisio.dashboard.usecase.training.super_memo.model.CardQueue
import org.junit.Assert
import org.junit.Test
import java.util.*

class CardQueueTest {

    private val sm = SuperMemo()

    @Test
    fun addCardTest() {
        val card1 = CardItem(sm, Date(1600001000))
        val card2 = CardItem(sm, Date(1600000000))
        val card3 = CardItem(sm, Date(1600002000))
        val queue = CardQueue()
        Assert.assertEquals(queue.queueCount(), 0)
        queue.addCard(card1)
        Assert.assertEquals(queue.nextCard(), card1)
        queue.addCard(card2)
        Assert.assertEquals(queue.nextCard(), card2)
        queue.addCard(card3)
        Assert.assertEquals(queue.nextCard(), card2)
    }

    @Test
    fun answerTest() {
        val card1 = CardItem(sm, Date(1600003000))
        val card2 = CardItem(sm, Date(1600001000))
        val queue = CardQueue()
        queue.addCard(card1)
        queue.addCard(card2)
        queue.answer(card1)
        Assert.assertEquals(queue.nextCard(), card2)
        Assert.assertEquals(queue.queueCount(), 2)
    }

}