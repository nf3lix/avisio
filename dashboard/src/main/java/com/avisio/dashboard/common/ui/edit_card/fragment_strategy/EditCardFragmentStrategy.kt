package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.text.TextUtils
import android.widget.Spinner
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.SaveCardConstraint
import com.avisio.dashboard.common.ui.edit_card.SaveCardValidator
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
        val question = questionFlexBox.getCardQuestion()
        val answer = answerFlexBox.getAnswer()
        val type = CardType.valueOf(typeSpinner.selectedItem.toString())
        val cardToValidate = Card(question = question, answer = answer, type = type)

        val unfulfilled = SaveCardValidator.getUnfulfilledConstraints(cardToValidate, this)
        if(unfulfilled.isEmpty()) {
            handleValidInput()
            Toast.makeText(fragment.requireContext(), "Karte wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
            return
        }

        for(constraint in unfulfilled) {
            constraint.targetInput.setError(fragment.requireContext().getString(constraint.notFulfilledMessageId))
        }
        /* if(!TextUtils.isEmpty(question.getStringRepresentation())
            && (!answer.answerIsEmpty() || type == CardType.CLOZE_TEXT)
            && !(type == CardType.CLOZE_TEXT && !question.hasQuestionToken())) {
            handleValidInput()
            Toast.makeText(fragment.requireContext(), "Karte wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
            return
        }

        if(TextUtils.isEmpty(question.getStringRepresentation())) {
            questionFlexBox.setError(fragment.requireContext().getString(R.string.create_card_empty_question))
        }
        if(answer.answerIsEmpty() && type != CardType.CLOZE_TEXT) {
            answerFlexBox.setError(fragment.requireContext().getString(R.string.create_card_empty_answer))
        }
        if(type == CardType.CLOZE_TEXT && !question.hasQuestionToken()) {
            questionFlexBox.setError(fragment.requireContext().getString(R.string.create_card_cloze_needs_at_least_one_question))
        }
        handleInvalidInput()


         */
    }

}