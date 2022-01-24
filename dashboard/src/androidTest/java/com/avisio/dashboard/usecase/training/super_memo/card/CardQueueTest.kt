package com.avisio.dashboard.usecase.training.super_memo.card

import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import com.avisio.dashboard.usecase.training.super_memo.model.CardItem
import com.avisio.dashboard.usecase.training.super_memo.model.CardQueue
import org.junit.Assert
import org.junit.Test
import java.util.*

class CardQueueTest {

    private val sm = SuperMemo()

    @Test
    fun addCardTest() {
        val card1 = CardItem(sm, dueDate = Date(1600001000L))
        val card2 = CardItem(sm, dueDate = Date(1600000000L))
        val card3 = CardItem(sm, dueDate = Date(1600002000L))
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
        val card1 = CardItem(sm, dueDate = Date(1600003000L))
        val card2 = CardItem(sm, dueDate = Date(1600001000L))
        val queue = CardQueue()
        queue.addCard(card1)
        queue.addCard(card2)
        queue.answer(card1)
        Assert.assertEquals(queue.nextCard(), card2)
        Assert.assertEquals(queue.queueCount(), 2)
    }

}