package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.type_change_handler

import android.view.View
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment

abstract class CardTypeChangeHandler(private val fragment: EditCardFragment) {
    abstract fun updateCardInputs()

    internal fun enableMarkdown() {
        fragment.questionInput.enableMarkdown()
        fragment.answerInput.enableMarkdown()
    }

    internal fun enableAnswerInput() {
        fragment.answerInput.resetInformation()
        fragment.answerInput.visibility = View.VISIBLE
        fragment.answerInput.isEnabled = true
    }

    companion object {

        fun getHandler(fragment: EditCardFragment, cardType: CardType): CardTypeChangeHandler = when(cardType) {
            CardType.STANDARD -> StandardCardTypeChangeHandler(fragment)
            CardType.CLOZE_TEXT -> ClozeTextCardTypeChangeHandler(fragment)
            CardType.STRICT -> StrictCardTypeChangeHandler(fragment)
        }

    }

}