package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.text.TextUtils
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import java.util.*

class CreateCardStrategy(
    private val fragment: EditCardFragment,
    private val card: Card,
    private val repository: CardRepository
) : EditCardFragmentStrategy(fragment) {

    override fun fillCardInformation() {
    }

    override fun saveCard() {
        val questionToken = CardQuestionToken(questionInput.text.toString(), CardQuestionTokenType.TEXT)
        val question = CardQuestion(arrayListOf(questionToken))
        val answer = CardAnswer(arrayListOf(answerInput.text.toString()))
        val cardToCreate = Card(
            boxId = card.boxId,
            createDate = Date(System.currentTimeMillis()),
            type = CardType.STANDARD,
            question = question,
            answer = answer
        )
        repository.insertCard(cardToCreate)
    }

    override fun handleValidInput() {
        saveCard()
        fragment.requireActivity().finish()
    }

    override fun handleInvalidInput() {
        Toast.makeText(fragment.requireContext(), R.string.create_card_empty_question_answer, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        if(!TextUtils.isEmpty(questionInput.text) || !TextUtils.isEmpty(answerInput.text)) {
            showOnBackPressedWarning()
            return
        }
        fragment.requireActivity().finish()
    }

}