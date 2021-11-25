package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.type_change_handler

import android.view.View
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment

class ClozeTextCardTypeChangeHandler(private val fragment: EditCardFragment) : CardTypeChangeHandler(fragment) {

    override fun updateCardInputs() {
        fragment.answerInput.disableMarkdown()
        fragment.answerInput.isEnabled = false
        if(!fragment.answerInput.getAnswer().answerIsEmpty()) {
            fragment.answerInput.setWarning(fragment.requireContext().getString(R.string.edit_card_cloze_text_answer_is_ignored))
        } else {
            fragment.answerInput.visibility = View.GONE
        }
        enableMarkdown()
    }

}