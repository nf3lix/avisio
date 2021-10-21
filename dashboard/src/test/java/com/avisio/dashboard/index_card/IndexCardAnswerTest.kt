package com.avisio.dashboard.index_card

import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardQuestion
import com.avisio.dashboard.common.data.model.card.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.CardQuestionTokenType
import org.junit.Assert
import org.junit.Test

class IndexCardAnswerTest {

    @Test
    fun equalsTrueTest() {
        val answer = CardAnswer(ArrayList())
        answer.addToken("TEST_1")
        answer.addToken("TEST_2")
        answer.addToken("TEST_3")
        val answer1 = CardAnswer(ArrayList())
        answer1.addToken("TEST_1")
        answer1.addToken("TEST_2")
        answer1.addToken("TEST_3")
        Assert.assertTrue(answer == answer1)
    }

    @Test
    fun equalsFalseTest() {
        val answer = CardAnswer(ArrayList())
        answer.addToken("TEST_1")
        answer.addToken("TEST_2")
        answer.addToken("TEST_3")
        val answer1 = CardAnswer(ArrayList())
        answer1.addToken("TEST_1")
        answer1.addToken("TEST_2")
        answer1.addToken("TEST_2")
        Assert.assertFalse(answer == answer1)
    }

}