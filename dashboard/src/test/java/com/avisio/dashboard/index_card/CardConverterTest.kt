package com.avisio.dashboard.index_card

import com.avisio.dashboard.common.data.database.converters.CardConverter
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import org.junit.Assert
import org.junit.Test

class CardConverterTest {

    private val converter: CardConverter = CardConverter()

    @Test
    fun questionConverterTest() {
        val question = CardQuestion(ArrayList())
        question.addToken(CardQuestionToken("test", CardQuestionTokenType.QUESTION))
        question.addToken(CardQuestionToken("test1", CardQuestionTokenType.IMAGE))
        question.addToken(CardQuestionToken("test2", CardQuestionTokenType.TEXT))
        val questionString = question.toString()
        val serialized = converter.questionToString(question)
        val deserialized = converter.stringToQuestion(serialized)
        Assert.assertEquals(questionString, deserialized.toString())
    }

    @Test
    fun answerConverterTest() {
        val answers = CardAnswer(arrayListOf("Test1", "Test2", "Test3"))
        val answerString = answers.toString()
        val serialized = converter.answerToString(answers)
        val deserialized = converter.stringToAnswer(serialized)
        Assert.assertEquals(answerString, deserialized.toString())
    }

}