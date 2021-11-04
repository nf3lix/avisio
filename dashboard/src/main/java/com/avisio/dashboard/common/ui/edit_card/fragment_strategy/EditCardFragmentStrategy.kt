package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.text.TextUtils
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment

abstract class EditCardFragmentStrategy(
    private val fragment: EditCardFragment
) {



    var questionInput: AppCompatEditText = fragment.requireView().findViewById(R.id.card_question_input)
    var answerInput: AppCompatEditText = fragment.requireView().findViewById(R.id.card_answer_input)

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
        val question = questionInput.text
        val answer = answerInput.text
        when(TextUtils.isEmpty(question) || TextUtils.isEmpty(answer)) {
            true -> {
                handleInvalidInput()
                Toast.makeText(fragment.requireContext(), R.string.create_card_empty_question_answer, Toast.LENGTH_LONG).show()
            }
            false -> {
                handleValidInput()
                Toast.makeText(fragment.requireContext(), "Karte wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
            }
        }
    }



}