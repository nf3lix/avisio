package com.avisio.dashboard.usecase.crud_card.common.fragment_strategy

import android.widget.Spinner
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.card.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardValidator
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.TargetInput.*

abstract class SaveCardTemplate(private val fragment: EditCardFragment, val titleId: Int) {

    companion object {

        fun getStrategy(fragment: EditCardFragment, card: Card, repository: CardRepository): SaveCardTemplate = when(fragment.workflow) {
            CRUD.CREATE -> CreateCardProcedure(fragment, card, repository)
            CRUD.UPDATE -> EditCardProcedure(fragment, card, repository)
        }

    }

    val questionFlexBox: QuestionFlexBox = fragment.requireView().findViewById(R.id.question_flexbox)
    val answerFlexBox: AnswerFlexBox = fragment.requireView().findViewById(R.id.answer_flex_box)
    val typeSpinner: Spinner = fragment.requireView().findViewById(R.id.card_type_spinner)

    abstract fun fillCardInformation()
    abstract fun saveCard(ignoreNextCardCheckBox: Boolean = false)
    abstract fun handleValidInput(ignoreNextCardCheckBox: Boolean = false)
    abstract fun handleInvalidInput()
    abstract fun onBackPressed()

    fun showOnBackPressedWarning() {
        val confirmDialog = ConfirmDialog(
            fragment.requireContext(),
            fragment.getString(R.string.create_card_cancel_dialog_title),
            fragment.getString(R.string.create_card_apply_changes),
            R.string.create_card_apply_changes_confirm,
            R.string.create_card_apply_changes_discard
        )
        confirmDialog.setOnConfirmListener {
            onFabClicked(ignoreNextCardCheckBox = true)
        }
        confirmDialog.setOnCancelListener {
            fragment.requireActivity().finish()
        }
        confirmDialog.showDialog()
    }

    fun onFabClicked(ignoreNextCardCheckBox: Boolean = false) {
        val question = questionFlexBox.getCardQuestion(trimmed = true)
        val answer = answerFlexBox.getAnswer()
        val type = fragment.getSelectedCardType()
        val cardToValidate = Card(question = question, answer = answer, type = type)

        val unfulfilled = SaveCardValidator.getUnfulfilledConstraints(cardToValidate)
        if(unfulfilled.isEmpty()) {
            handleValidInput(ignoreNextCardCheckBox)
            return
        }

        for(constraint in unfulfilled) {
            when(constraint.targetInput) {
                ANSWER_INPUT -> {
                    answerFlexBox.setError(fragment.requireContext().getString(constraint.notFulfilledMessageId))
                }
                QUESTION_INPUT -> {
                    questionFlexBox.setError(fragment.requireContext().getString(constraint.notFulfilledMessageId))
                }
            }
        }
    }

}