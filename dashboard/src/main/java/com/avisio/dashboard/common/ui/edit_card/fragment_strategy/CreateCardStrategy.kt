package com.avisio.dashboard.common.ui.edit_card.fragment_strategy

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
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
        val type = CardType.valueOf(typeSpinner.selectedItem.toString())
        val question = questionFlexBox.getCardQuestion()
        val answer = if(type == CardType.CLOZE_TEXT) CardAnswer.BLANK else answerFlexBox.getAnswer()
        val cardToCreate = Card(
            boxId = card.boxId,
            createDate = Date(System.currentTimeMillis()),
            type = type,
            question = question,
            answer = answer
        )
        repository.insertCard(cardToCreate)

        val checkboxView = fragment.requireActivity().findViewById<CheckBox>(R.id.checkbox_create_new_card)

        if(checkboxView.isChecked){
            val newIntent = Intent(fragment.requireContext(), CreateCardActivity::class.java)
            newIntent.putExtra(EditCardFragment.CARD_OBJECT_KEY, ParcelableCard(boxId = card.boxId))
            fragment.requireContext().startActivity(newIntent)
        }
    }

    override fun handleValidInput() {
        saveCard()
        fragment.requireActivity().finish()
        Toast.makeText(fragment.requireContext(), R.string.create_card_successful, Toast.LENGTH_LONG).show()
    }

    override fun handleInvalidInput() {
    }

    override fun onBackPressed() {
        if(!TextUtils.isEmpty(questionFlexBox.getCardQuestion().getStringRepresentation()) || !answerFlexBox.getAnswer().cardIsEmpty()) {
            showOnBackPressedWarning()
            return
        }
        fragment.requireActivity().finish()
    }

}