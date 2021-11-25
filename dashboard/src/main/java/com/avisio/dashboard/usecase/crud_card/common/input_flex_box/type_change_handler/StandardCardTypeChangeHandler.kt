package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.type_change_handler

import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment

class StandardCardTypeChangeHandler(private val fragment: EditCardFragment) : CardTypeChangeHandler(fragment) {

    override fun updateCardInputs() {
        enableMarkdown()
        fragment.questionInput.replaceClozeTextByStandardQuestion()
        enableAnswerInput()
    }

}