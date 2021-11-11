package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.text.TextUtils
import android.widget.Spinner
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.QuestionFlexBox

abstract class EditCardFragmentStrategy(
    private val fragment: EditCardFragment
) {

    val questionFlexBox: QuestionFlexBox = fragment.requireView().findViewById(R.id.question_flexbox)
    val answerFlexBox: AnswerFlexBox = fragment.requireView().findViewById(R.id.answer_flex_box)
    val typeSpinner: Spinner = fragment.requireView().findViewById(R.id.card_type_spinner)

    abstract fun fillCardInformation()
    abstract fun saveCard()
    abstract fun handleValidInput()
    abstract fun handleInvalidInput()
    abstract fun onBackPressed()

    fun showOnBackPressedWarning() {
        val confirmDialog = ConfirmDialog(
            fragment.requireContext(),
            fragment.getString(R.string.create_card_cancel_dialog_title),
            fragment.getString(R.string.create_card_cancel_dialog_message)
        )
        confirmDialog.setOnConfirmListener {
            fragment.requireActivity().finish()

        }
        confirmDialog.showDialog()
    }

    fun onFabClicked() {
        val question = questionFlexBox.getCardQuestion().getStringRepresentation()
        val answer = answerFlexBox.getAnswer()
        val type = CardType.valueOf(typeSpinner.selectedItem.toString())
        if(!TextUtils.isEmpty(question) && (!answer.cardIsEmpty() || type == CardType.CLOZE_TEXT)) {
            handleValidInput()
            Toast.makeText(fragment.requireContext(), "Karte wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
            return
        }

        if(TextUtils.isEmpty(question)) {
            questionFlexBox.setError(fragment.requireContext().getString(R.string.create_card_empty_question))
        }
        if(answer.cardIsEmpty() && type != CardType.CLOZE_TEXT) {
            answerFlexBox.setError(fragment.requireContext().getString(R.string.create_card_empty_answer))
        }
        handleInvalidInput()
    }

}