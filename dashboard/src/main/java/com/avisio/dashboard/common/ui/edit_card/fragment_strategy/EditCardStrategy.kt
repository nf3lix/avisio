package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment

class EditCardStrategy(
    private val fragment: EditCardFragment,
    private val card: Card,
    private val repository: CardRepository
) : EditCardFragmentStrategy(fragment) {

    override fun fillCardInformation() {
        questionFlexBox.setCardQuestion(card.question)
        answerFlexBox.setAnswer(card.answer)
        typeSpinner.setSelection(card.type.ordinal)
    }

    override fun saveCard() {
        val updatedQuestion = questionFlexBox.getCardQuestion()
        val updatedAnswer = answerFlexBox.getAnswer()
        val updatedType = CardType.valueOf(typeSpinner.selectedItem.toString())
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

    override fun handleValidInput() {
        saveCard()
        Toast.makeText(fragment.requireContext(), R.string.edit_card_successful, Toast.LENGTH_LONG).show()
    }

    override fun handleInvalidInput() {
    }

    override fun onBackPressed() {
        val updatedQuestion = questionFlexBox.getCardQuestion()
        val updatedAnswer = answerFlexBox.getAnswer()
        if(card.question != updatedQuestion || card.answer != updatedAnswer) {
            showOnBackPressedWarning()
            return
        }
        fragment.requireActivity().finish()
    }


}