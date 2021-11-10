package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.text.TextUtils
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.QuestionFlexBox
import com.google.android.material.textfield.TextInputLayout

abstract class EditCardFragmentStrategy(
    private val fragment: EditCardFragment
) {

    val questionFlexBox: QuestionFlexBox = fragment.requireView().findViewById(R.id.question_flexbox)
    private val answerInputLayout: TextInputLayout = fragment.requireView().findViewById(R.id.answer_text_input_layout)
    val answerInput: AppCompatEditText = fragment.requireView().findViewById(R.id.card_answer_input)
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
        val answer = answerInput.text
        if(!TextUtils.isEmpty(question) && !TextUtils.isEmpty(answer)) {
            handleValidInput()
            Toast.makeText(fragment.requireContext(), "Karte wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
            return
        }

        if(TextUtils.isEmpty(question)) {
            questionFlexBox.setError(fragment.requireContext().getString(R.string.create_card_empty_question))
        }
        if(TextUtils.isEmpty(answer)) {
            answerInputLayout.error = fragment.requireContext().getString(R.string.create_card_empty_answer)
        }
        handleInvalidInput()
    }

}