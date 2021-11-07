package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment

class EditCardStrategy(
    private val fragment: EditCardFragment,
    private val card: Card,
    private val repository: CardRepository
) : EditCardFragmentStrategy(fragment) {

    override fun fillCardInformation() {
        questionInput.setText(card.question.getStringRepresentation())
        answerInput.setText(card.answer.getStringRepresentation())
    }

    override fun saveCard() {
        val updatedQuestion = CardQuestion.getFromStringRepresentation(questionInput.text.toString())
        val updatedAnswer = CardAnswer.getFromStringRepresentation(answerInput.text.toString())
        if(card.question != updatedQuestion || card.answer != updatedAnswer) {
            val updatedCard = Card(
                card.id,
                card.boxId,
                card.createDate,
                card.type,
                updatedQuestion,
                updatedAnswer
            )
            repository.updateCard(updatedCard)
        }
        fragment.requireActivity().finish()
    }

    override fun handleValidInput() {
        saveCard()
        Toast.makeText(fragment.requireContext(), "Karte wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
    }

    override fun handleInvalidInput() {
        Toast.makeText(fragment.requireContext(), R.string.create_card_empty_question_answer, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        val updatedQuestion = CardQuestion.getFromStringRepresentation(questionInput.text.toString())
        val updatedAnswer = CardAnswer.getFromStringRepresentation(answerInput.text.toString())
        if(card.question != updatedQuestion || card.answer != updatedAnswer) {
            showOnBackPressedWarning()
            return
        }
        fragment.requireActivity().finish()
    }


}