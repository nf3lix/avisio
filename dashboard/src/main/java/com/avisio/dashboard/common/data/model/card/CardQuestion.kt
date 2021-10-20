package com.avisio.dashboard.common.data.model.card

import java.lang.StringBuilder

class CardQuestion(private val tokenList: ArrayList<CardQuestionToken>) {

    fun addToken(token: CardQuestionToken) {
        tokenList.add(token)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for(token in tokenList) {
            sb.append(token.toString())
        }
        return sb.toString()
    }

}