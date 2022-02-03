package com.avisio.dashboard.usecase.crud_card.common.fragment_strategy

import android.widget.Spinner
import android.widget.Toast
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

abstract class CardFragmentStrategy(private val fragment: EditCardFragment, val titleId: Int) {

    companion object {

        fun getStrategy(fragment: EditCardFragment, card: Card, repository: CardRepository): CardFragmentStrategy = when(fragment.workflow) {
            CRUD.CREATE -> CreateCardStrategy(fragment, card, repository)
            CRUD.UPDATE -> EditCardStrategy(fragment, card, repository)
        }

    }

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
        val question = questionFlexBox.getCardQuestion(trimmed = true)
        val answer = answerFlexBox.getAnswer()
        val type = fragment.getSelectedCardType()
        val cardToValidate = Card(question = question, answer = answer, type = type)

        val unfulfilled = SaveCardValidator.getUnfulfilledConstraints(cardToValidate)
        if(unfulfilled.isEmpty()) {
            handleValidInput()
            Toast.makeText(fragment.requireContext(), "Karte wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
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