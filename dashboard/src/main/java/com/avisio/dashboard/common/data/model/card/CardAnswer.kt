package com.avisio.dashboard.common.data.model.card

import java.lang.StringBuilder

class CardAnswer(private val answerList: ArrayList<String>) {

    fun addToken(answer: String) {
        answerList.add(answer)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for(token in answerList) {
            sb.append("$token;")
        }
        return sb.toString()
    }

}