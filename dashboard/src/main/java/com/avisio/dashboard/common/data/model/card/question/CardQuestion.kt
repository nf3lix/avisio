package com.avisio.dashboard.common.data.model.card.question

class CardQuestion(val tokenList: ArrayList<QuestionToken>) {

    companion object {
        fun getFromStringRepresentation(representation: String): CardQuestion {
            return CardQuestion(arrayListOf(QuestionToken(representation, QuestionTokenType.TEXT)))
        }
    }

    fun getTokensOfType(tokenType: QuestionTokenType): List<QuestionToken> {
        val typeSpecificTokenList = arrayListOf<QuestionToken>()
        for(questionToken in tokenList) {
            if(questionToken.tokenType == tokenType) {
                typeSpecificTokenList.add(questionToken)
            }
        }
        return typeSpecificTokenList
    }

    fun hasTokenOfType(tokenType: QuestionTokenType): Boolean {
        for(token in tokenList) {
            if(token.tokenType == tokenType) {
                return true
            }
        }
        return false
    }

    fun addToken(token: QuestionToken) {
        tokenList.add(token)
    }

    fun getStringRepresentation(): String {
        val representation = StringBuilder()
        for(token in tokenList) {
            representation.append(token.content + " ")
        }
        return representation.toString().trim()
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