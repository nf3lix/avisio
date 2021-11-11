package com.avisio.dashboard.common.data.model.card

class CardAnswer(private val answerList: ArrayList<String>) {

    companion object {
        fun getFromStringRepresentation(representation: String): CardAnswer {
            return CardAnswer(arrayListOf(representation))
        }
        val BLANK = CardAnswer(arrayListOf())
    }

    fun addToken(answer: String) {
        answerList.add(answer)
    }

    fun getStringRepresentation(): String {
        val representation = StringBuilder()
        for(answer in answerList) {
            representation.append("$answer ")
        }
        return representation.toString().trim()
    }

    fun cardIsEmpty(): Boolean {
        return getStringRepresentation().isEmpty()
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

    override fun hashCode(): Int {
        return answerList.hashCode()
    }

}