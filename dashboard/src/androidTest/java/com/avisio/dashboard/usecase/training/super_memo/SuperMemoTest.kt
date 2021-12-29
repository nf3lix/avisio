package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.CardItem
import org.junit.Assert
import org.junit.Test

class SuperMemoTest {

    private val sm = SuperMemo()

    @Test
    fun answerGradeOverThresholdRecall() {
        sm.queue().addCard(CardItem(sm))
        sm.queue().addCard(CardItem(sm))
        sm.queue().addCard(CardItem(sm))
        val c1 = sm.queue().nextCard()!!
        sm.answer(3.0, c1)
        val c2 = sm.queue().nextCard()!!
        sm.answer(4.0, c2)
        val c3 = sm.queue().nextCard()!!
        sm.answer(5.0, c3)
        Assert.assertEquals((c1.dueDate().time - c1.previousDate()!!.time).toDouble(), 14427904.0, 10000.0)
        Assert.assertEquals((c2.dueDate().time - c2.previousDate()!!.time).toDouble(), 14427904.0, 10000.0)
        Assert.assertEquals((c3.dueDate().time - c3.previousDate()!!.time).toDouble(), 14427904.0, 10000.0)
    }

    @Test
    fun answerUnderThresholdRecall() {
        sm.queue().addCard(CardItem(sm))
        sm.queue().addCard(CardItem(sm))
        sm.queue().addCard(CardItem(sm))
        val c1 = sm.queue().nextCard()!!
        sm.answer(0.0, c1)
        val c2 = sm.queue().nextCard()!!
        sm.answer(1.0, c2)
        val c3 = sm.queue().nextCard()!!
        sm.answer(2.0, c3)
        Assert.assertTrue((c1.dueDate().time).toDouble() - System.currentTimeMillis() <= 0)
        Assert.assertTrue((c2.dueDate().time).toDouble() - System.currentTimeMillis() <= 0)
        Assert.assertTrue((c3.dueDate().time).toDouble() - System.currentTimeMillis() <= 0)
        Assert.assertEquals(c1.previousDate(), null)
        Assert.assertEquals(c2.previousDate(), null)
        Assert.assertEquals(c3.previousDate(), null)
    }

}