package com.avisio.dashboard.common.data.model.card

import java.lang.StringBuilder

class CardQuestion(val tokenList: ArrayList<CardQuestionToken>) {

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

    override fun equals(other: Any?): Boolean {
        if(other !is CardQuestion) {
            return false
        }
        if(tokenList.size != other.tokenList.size) {
            return false
        }
        for((index, token) in tokenList.withIndex()) {
            if(token.content != other.tokenList[index].content) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return tokenList.hashCode()
    }


}