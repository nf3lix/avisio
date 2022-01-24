package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.card.CardItemTest
import com.avisio.dashboard.usecase.training.super_memo.model.CardItem
import org.junit.Assert
import org.junit.Test
import java.util.*

class SuperMemoTest {

    private val sm = SuperMemo()

    @Test
    fun maxGradeAnswerTest() {
        val interval1 = 14427904 // ~ 4h
        val interval2 = 17315486 // ~ 4h 45m

        val answer1 = 1600000000000
        val answer2 = nextAnswerDate(answer1, interval1)

        sm.queue().addCard(CardItem(sm))
        sm.queue().addCard(CardItem(sm))

        val card11 = sm.queue().nextCard()!!
        sm.answer(5.0, card11, Date(answer1))

        val card21 = sm.queue().nextCard()!!
        sm.answer(5.0, card21, Date(answer2))

        val card12 = sm.queue().nextCard()!!
        sm.answer(5.0, card12, Date(answer1))
        Assert.assertEquals(sm.forgettingCurves().curves()[0][0].graph()!!.a, -0.2961760279708177, 1E-3)
        Assert.assertEquals(sm.forgettingCurves().curves()[0][0].graph()!!.c, 4.842871655888891, 1E-3)

        val card22 = sm.queue().nextCard()!!
        sm.answer(5.0, card22, Date(answer1))
        Assert.assertEquals(sm.forgettingCurves().curves()[0][0].graph()!!.a, -0.2961760279708177, 1E-3)
        Assert.assertEquals(sm.forgettingCurves().curves()[0][0].graph()!!.c, 4.842871655888891, 1E-3)
    }

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
        Assert.assertTrue(c1.dueDate().time.toDouble() - System.currentTimeMillis() <= 0)
        Assert.assertTrue(c2.dueDate().time.toDouble() - System.currentTimeMillis() <= 0)
        Assert.assertTrue(c3.dueDate().time.toDouble() - System.currentTimeMillis() <= 0)
        Assert.assertEquals(c1.previousDate(), null)
        Assert.assertEquals(c2.previousDate(), null)
        Assert.assertEquals(c3.previousDate(), null)
    }

    private fun nextAnswerDate(prevAnswer: Long, interval: Int): Long {
        return (prevAnswer + interval + CardItemTest.DUE_DATE_OFFSET).toLong()
    }

}