package com.avisio.dashboard.index_card

import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import org.junit.Assert
import org.junit.Test

class IndexCardQuestionTest {

    @Test
    fun equalsTrueTest() {
        val question = CardQuestion(ArrayList())
        question.addToken(CardQuestionToken("test", CardQuestionTokenType.QUESTION))
        question.addToken(CardQuestionToken("test1", CardQuestionTokenType.QUESTION))
        question.addToken(CardQuestionToken("test2", CardQuestionTokenType.QUESTION))
        val question2 = CardQuestion(ArrayList())
        question2.addToken(CardQuestionToken("test", CardQuestionTokenType.QUESTION))
        question2.addToken(CardQuestionToken("test1", CardQuestionTokenType.QUESTION))
        question2.addToken(CardQuestionToken("test2", CardQuestionTokenType.QUESTION))
        Assert.assertTrue(question == question2)
    }

    @Test
    fun equalsFalseTest() {
        val question = CardQuestion(ArrayList())
        question.addToken(CardQuestionToken("test", CardQuestionTokenType.QUESTION))
        question.addToken(CardQuestionToken("test1", CardQuestionTokenType.QUESTION))
        question.addToken(CardQuestionToken("test2", CardQuestionTokenType.QUESTION))
        val question2 = CardQuestion(ArrayList())
        question2.addToken(CardQuestionToken("test", CardQuestionTokenType.QUESTION))
        question2.addToken(CardQuestionToken("test", CardQuestionTokenType.QUESTION))
        question2.addToken(CardQuestionToken("test2", CardQuestionTokenType.QUESTION))
        Assert.assertFalse(question == question2)
    }

}