package com.avisio.dashboard.index_card

import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import org.junit.Assert
import org.junit.Test

class IndexCardQuestionTest {

    @Test
    fun equalsTrueTest() {
        val question = CardQuestion(ArrayList())
        question.addToken(QuestionToken("test", QuestionTokenType.QUESTION))
        question.addToken(QuestionToken("test1", QuestionTokenType.QUESTION))
        question.addToken(QuestionToken("test2", QuestionTokenType.QUESTION))
        val question2 = CardQuestion(ArrayList())
        question2.addToken(QuestionToken("test", QuestionTokenType.QUESTION))
        question2.addToken(QuestionToken("test1", QuestionTokenType.QUESTION))
        question2.addToken(QuestionToken("test2", QuestionTokenType.QUESTION))
        Assert.assertTrue(question == question2)
    }

    @Test
    fun equalsFalseTest() {
        val question = CardQuestion(ArrayList())
        question.addToken(QuestionToken("test", QuestionTokenType.QUESTION))
        question.addToken(QuestionToken("test1", QuestionTokenType.QUESTION))
        question.addToken(QuestionToken("test2", QuestionTokenType.QUESTION))
        val question2 = CardQuestion(ArrayList())
        question2.addToken(QuestionToken("test", QuestionTokenType.QUESTION))
        question2.addToken(QuestionToken("test", QuestionTokenType.QUESTION))
        question2.addToken(QuestionToken("test2", QuestionTokenType.QUESTION))
        Assert.assertFalse(question == question2)
    }

}