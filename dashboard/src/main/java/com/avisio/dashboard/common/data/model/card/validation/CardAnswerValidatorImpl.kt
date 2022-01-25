package com.avisio.dashboard.common.data.model.card.validation

import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType.QUESTION
import com.avisio.dashboard.usecase.training.QuestionResult

class CardAnswerValidatorImpl(
    private val card: Card,
    private val givenAnswer: CardAnswer,
    cardType: CardType) : CardAnswerValidator(cardType)
{

    override fun getStrictCardAnswerResult(): QuestionResult {
        return when(card.answer.getStringRepresentation() == givenAnswer.getStringRepresentation()) {
            true -> QuestionResult.EASY
            false -> QuestionResult.WRONG
        }
    }

    override fun getStandardCardAnswerResult(): QuestionResult {
        return QuestionResult.MEDIUM
    }

    override fun getClozeTextCardAnswerResult(): QuestionResult {
        val questionTokenList = card.question.getTokensOfType(QUESTION)
        return answerTokensMatch(questionTokenList)
    }

    private fun answerTokensMatch(questionTokenList: List<QuestionToken>): QuestionResult {
        if(!answerListSizeMatches(questionTokenList)) return QuestionResult.WRONG
        for((index, questionToken) in questionTokenList.withIndex()) {
            if(questionToken.content != givenAnswer.answerList[index]) {
                return QuestionResult.WRONG
            }
        }
        return QuestionResult.EASY
    }

    private fun answerListSizeMatches(questionTokenList: List<QuestionToken>): Boolean {
        return givenAnswer.answerList.size == questionTokenList.size
    }

}