package com.avisio.dashboard.usecase.crud_card.common.fragment_strategy

import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.persistence.card.CardRepository
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment

class EditCardProcedure(
    private val fragment: EditCardFragment,
    private val card: Card,
    private val repository: CardRepository
) : SaveCardTemplate(fragment, R.string.edit_card_action_bar_title) {

    override fun fillCardInformation() {
        fragment.requireActivity().findViewById<CheckBox>(R.id.checkbox_create_new_card).visibility = View.GONE
        questionFlexBox.setCardQuestion(card.question)
        answerFlexBox.setAnswer(card.answer)
        typeSpinner.setSelection(card.type.ordinal)
        if(card.type == CardType.CLOZE_TEXT) {
            answerFlexBox.visibility = View.GONE
        }
    }

    override fun saveCard(ignoreNextCardCheckBox: Boolean) {
        val updatedQuestion = questionFlexBox.getCardQuestion()
        val updatedAnswer = answerFlexBox.getAnswer()
        val updatedType = fragment.getSelectedCardType()
        val newCard = Card(question = updatedQuestion, answer = updatedAnswer, type = updatedType)
        if(cardChanged(card, newCard)) {
            val updatedCard = Card(
                card.id,
                card.boxId,
                card.createDate,
                updatedType,
                updatedQuestion,
                updatedAnswer
            )
            repository.updateCard(updatedCard)
        }
        fragment.requireActivity().finish()
    }

    private fun cardChanged(oldCard: Card, newCard: Card): Boolean {
        return oldCard.question != newCard.question
                || oldCard.answer != newCard.answer
                || oldCard.type != newCard.type
    }

    override fun handleValidInput(ignoreNextCardCheckBox: Boolean) {
        saveCard()
        Toast.makeText(fragment.requireContext(), R.string.edit_card_successful, Toast.LENGTH_LONG).show()
    }

    override fun handleInvalidInput() {
    }

    override fun onBackPressed() {
        val updatedQuestion = questionFlexBox.getCardQuestion()
        val updatedAnswer = answerFlexBox.getAnswer()
        if(card.question != updatedQuestion || card.answer.getStringRepresentation() != updatedAnswer.getStringRepresentation()) {
            showOnBackPressedWarning()
            return
        }
        fragment.requireActivity().finish()
    }


}