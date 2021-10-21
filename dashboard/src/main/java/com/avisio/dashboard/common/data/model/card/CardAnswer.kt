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

    override fun equals(other: Any?): Boolean {
        if(other !is CardAnswer) {
            return false
        }
        if(answerList.size != other.answerList.size) {
            return false
        }
        for((index, answer) in answerList.withIndex()) {
            if(answer != other.answerList[index]) {
                return false
            }
        }
        return true
    }

}