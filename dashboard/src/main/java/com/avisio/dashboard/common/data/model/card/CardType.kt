package com.avisio.dashboard.common.data.model.card

import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.validation.CardAnswerValidatorImpl
import com.avisio.dashboard.usecase.training.QuestionResult

enum class CardType(val iconId: Int) {

    STRICT(R.drawable.card_icon_standard),
    CLOZE_TEXT(R.drawable.card_icon_cloze_text),
    STANDARD(R.drawable.card_icon_custom);

    fun getQuestionResult(cardQuestion: CardQuestion, cardAnswer: CardAnswer): QuestionResult {
        return CardAnswerValidatorImpl(
            cardQuestion,
            cardAnswer,
            this
        ).getCardAnswerResult()
    }

}