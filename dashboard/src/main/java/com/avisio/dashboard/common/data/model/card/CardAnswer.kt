package com.avisio.dashboard.common.data.model.card

data class CardAnswer(val answerList: ArrayList<String>, val imagePath: String? = null) {

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

    fun answerIsEmpty(): Boolean {
        return getStringRepresentation().isEmpty()
    }

    fun hasImage(): Boolean {
        return imagePath != null
    }

}