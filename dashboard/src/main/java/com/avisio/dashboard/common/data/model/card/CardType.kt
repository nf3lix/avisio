package com.avisio.dashboard.common.data.model.card

import android.content.Context
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.validation.CardAnswerValidatorImpl
import com.avisio.dashboard.usecase.training.QuestionResult

enum class CardType(val iconId: Int, private val displayNameId: Int) {

    STRICT(R.drawable.card_icon_standard, R.string.card_type_strict),
    CLOZE_TEXT(R.drawable.card_icon_cloze_text, R.string.card_type_cloze_text),
    STANDARD(R.drawable.card_icon_custom, R.string.card_type_standard);

    companion object {

        fun displayNames(context: Context): Array<String> {
            val names = arrayListOf<String>()
            for(value in values()) {
                names.add(value.displayedName(context))
            }
            return names.toList().toTypedArray()
        }

        fun valueWithDisplayName(displayName: String, context: Context): CardType? {
            for(value in values()) {
                if(value.displayedName(context).contentEquals(displayName)) {
                    return value
                }
            }
            return null
        }

    }

    fun getQuestionResult(card: Card, cardAnswer: CardAnswer): QuestionResult {
        return CardAnswerValidatorImpl(
            card,
            cardAnswer,
            this
        ).getCardAnswerResult()
    }

    fun displayedName(context: Context): String {
        return context.getString(displayNameId)
    }

}