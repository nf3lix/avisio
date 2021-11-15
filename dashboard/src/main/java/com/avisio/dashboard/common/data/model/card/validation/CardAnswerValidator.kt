package com.avisio.dashboard.common.data.model.card.validation

import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.usecase.training.QuestionResult

abstract class CardAnswerValidator(private val cardType: CardType) {

    fun getCardAnswerResult(): QuestionResult = when(cardType) {
        CardType.STRICT -> getStandardCardAnswerResult()
        CardType.CLOZE_TEXT -> getClozeTextCardAnswerResult()
        CardType.STANDARD -> getCustomCardAnswerResult()
    }

    abstract fun getStandardCardAnswerResult(): QuestionResult
    abstract fun getClozeTextCardAnswerResult(): QuestionResult
    abstract fun getCustomCardAnswerResult(): QuestionResult

}