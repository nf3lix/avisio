package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.content.Intent
import android.text.TextUtils
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.create_card.CreateCardActivity
import java.util.*


class CreateCardStrategy(
    private val fragment: EditCardFragment,
    private val card: Card,
    private val repository: CardRepository,
) : EditCardFragmentStrategy(fragment) {

    override fun fillCardInformation() {
    }

    override fun saveCard() {
        val questionToken = CardQuestionToken(questionInput.text.toString(), CardQuestionTokenType.TEXT)
        val question = CardQuestion(arrayListOf(questionToken))
        val answer = CardAnswer(arrayListOf(answerInput.text.toString()))
        val type = CardType.valueOf(typeSpinner.selectedItem.toString())
        val cardToCreate = Card(
            boxId = card.boxId,
            createDate = Date(System.currentTimeMillis()),
            type = type,
            question = question,
            answer = answer
        )
        repository.insertCard(cardToCreate)

        val checkboxView = fragment.requireActivity().findViewById<CheckBox>(com.avisio.dashboard.R.id.checkbox_create_new_card)

        if(checkboxView.isChecked){
            val newIntent = Intent(fragment.requireContext(), CreateCardActivity::class.java)
            newIntent.putExtra(EditCardFragment.CARD_OBJECT_KEY, ParcelableCard(boxId = card.boxId))
            fragment.requireContext().startActivity(newIntent)
        }
    }

    override fun handleValidInput() {
        saveCard()
        fragment.requireActivity().finish()
    }

    override fun handleInvalidInput() {
    }

    override fun onBackPressed() {
        if(!TextUtils.isEmpty(questionInput.text) || !TextUtils.isEmpty(answerInput.text)) {
            showOnBackPressedWarning()
            return
        }
        fragment.requireActivity().finish()
    }

}