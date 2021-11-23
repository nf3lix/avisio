package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.type_change_handler

import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment

class StrictCardTypeChangeHandler(private val fragment: EditCardFragment) : CardTypeChangeHandler(fragment) {

    override fun updateCardInputs() {
        fragment.questionInput.disableMarkdown()
        fragment.questionInput.replaceClozeTextByStandardQuestion()
        enableAnswerInput()
        fragment.answerInput.disableMarkdown()
    }

}