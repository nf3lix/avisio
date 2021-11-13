package com.avisio.dashboard.common.data.model.card.validation

import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType.QUESTION
import com.avisio.dashboard.usecase.training.QuestionResult

class CardAnswerValidatorImpl(
    private val question: CardQuestion,
    private val answer: CardAnswer,
    cardType: CardType) : CardAnswerValidator(cardType)
{

    override fun getStandardCardAnswerResult(): QuestionResult {
        return when(question.getStringRepresentation() == answer.getStringRepresentation()) {
            true -> QuestionResult.CORRECT
            false -> QuestionResult.INCORRECT
        }
    }

    override fun getCustomCardAnswerResult(): QuestionResult {
        return QuestionResult.PARTIALLY_CORRECT
    }

    override fun getClozeTextCardAnswerResult(): QuestionResult {
        val questionTokenList = question.getTokensOfType(QUESTION)
        return answerTokensMatch(questionTokenList)
    }

    private fun answerTokensMatch(questionTokenList: List<QuestionToken>): QuestionResult {
        if(!answerListSizeMatches(questionTokenList)) return QuestionResult.INCORRECT
        for((index, questionToken) in questionTokenList.withIndex()) {
            if(questionToken.content != answer.answerList[index]) {
                return QuestionResult.INCORRECT
            }
        }
        return QuestionResult.CORRECT
    }

    private fun answerListSizeMatches(questionTokenList: List<QuestionToken>): Boolean {
        return answer.answerList.size == questionTokenList.size
    }

}