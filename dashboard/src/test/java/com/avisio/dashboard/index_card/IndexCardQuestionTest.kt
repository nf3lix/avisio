package com.avisio.dashboard.index_card

import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardQuestion
import com.avisio.dashboard.common.data.model.card.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.CardQuestionTokenType
import com.google.gson.Gson
import org.junit.Test


class IndexCardQuestionTest {

    @Test
    fun serialize() {
        val question = CardQuestion(ArrayList())
        question.addToken(CardQuestionToken("test", CardQuestionTokenType.QUESTION))
        question.addToken(CardQuestionToken("test1", CardQuestionTokenType.QUESTION))
        question.addToken(CardQuestionToken("test2", CardQuestionTokenType.QUESTION))
        val gson = Gson()
        val txt = gson.toJson(question)
        println(question.toString())
        println(txt.toString())
        val t = gson.fromJson(txt, CardQuestion::class.java)
        println(t.toString())
    }

    @Test
    fun serializeAnswer() {
        val answers = CardAnswer(arrayListOf("Test1", "Test2", "Test3"))
        val gson = Gson()
        val txt = gson.toJson(answers)
        println(answers.toString())
        println(txt.toString())
        val t = gson.fromJson(txt, CardAnswer::class.java)
        println(t.toString())
    }

}