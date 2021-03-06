package com.avisio.dashboard.usecase.crud_card.common.fragment_strategy

import android.content.Intent
import android.text.TextUtils
import android.widget.CheckBox
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.transfer.setCardObject
import com.avisio.dashboard.common.persistence.card.CardRepository
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.create.CreateCardActivity
import java.util.*


class CreateCardProcedure(
    private val fragment: EditCardFragment,
    private val card: Card,
    private val repository: CardRepository,
) : SaveCardTemplate(fragment, R.string.create_card_action_bar_title) {

    override fun fillCardInformation() {
        typeSpinner.setSelection(card.type.ordinal)
        fragment.onCardTypeSet(CardType.values()[card.type.ordinal])
    }

    override fun saveCard(ignoreNextCardCheckBox: Boolean) {
        val type = fragment.getSelectedCardType()
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

        if(checkboxView.isChecked && !ignoreNextCardCheckBox) {
            val newIntent = Intent(fragment.requireContext(), CreateCardActivity::class.java)
            newIntent.setCardObject(card)
            fragment.requireContext().startActivity(newIntent)
        }
    }

    override fun handleValidInput(ignoreNextCardCheckBox: Boolean) {
        saveCard(ignoreNextCardCheckBox)
        fragment.requireActivity().finish()
        Toast.makeText(fragment.requireContext(), R.string.create_card_successful, Toast.LENGTH_LONG).show()
    }

    override fun handleInvalidInput() {
    }

    override fun onBackPressed() {
        if(!TextUtils.isEmpty(questionFlexBox.getCardQuestion().getStringRepresentation()) || !answerFlexBox.getAnswer().answerIsEmpty()) {
            showOnBackPressedWarning()
            return
        }
        fragment.requireActivity().finish()
    }

}