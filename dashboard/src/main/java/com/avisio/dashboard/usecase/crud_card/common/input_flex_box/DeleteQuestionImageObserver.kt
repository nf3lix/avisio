package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.ui.card_image.CardImage

class DeleteQuestionImageObserver(private val questionInput: QuestionFlexBox) : CardImage.DeleteImageClickListener {

    override fun onClick() {
        val newTokens = arrayListOf<QuestionToken>()
        for(token in questionInput.getCardQuestion().tokenList) {
            if(token.tokenType != QuestionTokenType.IMAGE) {
                newTokens.add(token)
            }
        }
        questionInput.setCardQuestion(CardQuestion(newTokens))
    }

}