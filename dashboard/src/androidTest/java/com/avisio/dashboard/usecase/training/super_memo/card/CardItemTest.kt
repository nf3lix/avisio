package com.avisio.dashboard.usecase.training.super_memo.card

import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import com.avisio.dashboard.usecase.training.super_memo.model.CardItem
import org.junit.Assert
import org.junit.Test
import java.util.*
class CardItemTest {

    companion object {
        const val DUE_DATE_OFFSET = 1E4
    }

    @Test
    fun increaseRepetition() {
        val card = CardItem(SuperMemo())
        Assert.assertEquals(card.repetition(), -1)
        card.answer(3.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.repetition(), 0)
        card.answer(4.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.repetition(), 1)
        card.answer(5.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.repetition(), 2)
    }

    @Test
    fun doNotIncreaseRepetitionUnderGradeThreshold() {
        val card = CardItem(SuperMemo())
        Assert.assertEquals(card.repetition(), -1)
        card.answer(0.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.repetition(), -1)
        card.answer(1.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.repetition(), -1)
        card.answer(2.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.repetition(), -1)
    }

    @Test
    fun increaseLapse() {
        val card = CardItem(SuperMemo())
        Assert.assertEquals(card.lapse(), 0)
        card.answer(0.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.lapse(), 1)
        card.answer(1.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.lapse(), 2)
        card.answer(2.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.lapse(), 3)
    }

    @Test
    fun doNotIncreaseLapseOverGradeThreshold() {
        val card = CardItem(SuperMemo())
        Assert.assertEquals(card.lapse(), 0)
        card.answer(3.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.lapse(), 0)
        card.answer(4.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.lapse(), 0)
        card.answer(5.0, Date(System.currentTimeMillis()))
        Assert.assertEquals(card.lapse(), 0)
    }

    @Test
    fun perfectAnswersInARow() {
        val interval1 = 14427904 // ~ 4h
        val interval2 = 17315486 // ~ 4h 45m
        val interval3 = 89910717 // ~ 25h
        val interval4 = 381306380 // ~ 4d 10h

        val answer1 = 1600000000000
        val answer2 = nextAnswerDate(answer1, interval1)
        val answer3 = nextAnswerDate(answer2, interval2)
        val answer4 = nextAnswerDate(answer3, interval3)

        val card = CardItem(SuperMemo())
        card.answer(5.0, Date(answer1))
        Assert.assertEquals((card.dueDate().time - answer1).toDouble(), interval1.toDouble(), 10000.0)

        card.answer(5.0, Date(answer2))
        Assert.assertEquals((card.dueDate().time - answer2).toDouble(), interval2.toDouble(), 10000.0)

        card.answer(5.0, Date(answer3))
        Assert.assertEquals((card.dueDate().time - answer3).toDouble(), interval3.toDouble(), 10000.0)

        card.answer(5.0, Date(answer4))
        Assert.assertEquals((card.dueDate().time - answer4).toDouble(), interval4.toDouble(), 10000.0)
    }

    @Test
    fun difficultCorrectAnswersInARow() {
        val interval1 = 14427905 // ~ 4h
        val interval2 = 17315486 // ~ 4h 45m
        val interval3 = 49779504 // ~ 14h
        val interval4 = 91526291 // ~ 1d 1h 30m

        val answer1 = 1600000000000
        val answer2 = nextAnswerDate(answer1, interval1)
        val answer3 = nextAnswerDate(answer2, interval2)
        val answer4 = nextAnswerDate(answer3, interval3)

        val card = CardItem(SuperMemo())
        card.answer(3.0, Date(answer1))
        Assert.assertEquals((card.dueDate().time - answer1).toDouble(), interval1.toDouble(), 10000.0)

        card.answer(3.0, Date(answer2))
        Assert.assertEquals((card.dueDate().time - answer2).toDouble(), interval2.toDouble(), 10000.0)

        card.answer(3.0, Date(answer3))
        Assert.assertEquals((card.dueDate().time - answer3).toDouble(), interval3.toDouble(), 10000.0)

        card.answer(3.0, Date(answer4))
        Assert.assertEquals((card.dueDate().time - answer4).toDouble(), interval4.toDouble(), 10000.0)
    }

    @Test
    fun wrongAnswersAfterCorrectAnswersInARow() {
        val interval1 = 14427904 // ~ 4h
        val interval2 = 17315486 // ~ 4h 45m
        val interval3 = 89910717 // ~ 25h
        val interval4 = 15003141 // ~ 4h 15m

        val answer1 = 1600000000000
        val answer2 = nextAnswerDate(answer1, interval1)
        val answer3 = nextAnswerDate(answer2, interval2)
        val answer4 = nextAnswerDate(answer3, interval3)

        val card = CardItem(SuperMemo())
        card.answer(5.0, Date(answer1))
        card.answer(5.0, Date(answer2))

        card.answer(0.0, Date(answer3))
        Assert.assertEquals((card.dueDate().time - answer3).toDouble(), 0.0, 0.0)

        card.answer(5.0, Date(answer4))
        Assert.assertEquals((card.dueDate().time - answer4).toDouble(), interval4.toDouble(), 10000.0)
    }

    @Test
    fun correctAnswerAfterWrongAnswersInARow() {
        val interval1 = 10000
        val interval4 = 16870152 // ~ 4h 40m

        val answer1 = 1600000000000
        val answer2 = nextAnswerDate(answer1, interval1)
        val answer3 = nextAnswerDate(answer2, interval1)

        val card = CardItem(SuperMemo())
        card.answer(0.0, Date(answer1))
        card.answer(0.0, Date(answer2))
        card.answer(0.0, Date(answer2))
        card.answer(0.0, Date(answer2))

        card.answer(5.0, Date(answer3))
        Assert.assertEquals((card.dueDate().time - answer3).toDouble(), interval4.toDouble(), 10000.0)
    }

    private fun nextAnswerDate(prevAnswer: Long, interval: Int): Long {
        return (prevAnswer + interval + DUE_DATE_OFFSET).toLong()
    }

}

