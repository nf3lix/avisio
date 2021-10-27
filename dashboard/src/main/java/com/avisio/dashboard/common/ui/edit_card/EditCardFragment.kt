package com.avisio.dashboard.common.ui.edit_card

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.*
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class EditCardFragment : Fragment() {

    companion object {
        const val FRAGMENT_MODE_KEY: String = "EDIT_CARD_FRAGMENT_MODE"
        const val CARD_OBJECT_KEY: String = "CARD_OBJECT"
    }

    private lateinit var questionInput: AppCompatEditText
    private lateinit var answerInput: AppCompatEditText

    private var fragmentMode: EditCardFragmentMode = EditCardFragmentMode.CREATE_CARD
    private lateinit var card: Card

    private lateinit var cardRepository: CardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        cardRepository = CardRepository(requireActivity().application)
        arguments?.let {
            fragmentMode = EditCardFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
            card = it.getCardObject()!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_card, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupFab()
        questionInput = requireView().findViewById(R.id.card_question_input)!!
        answerInput = requireView().findViewById(R.id.card_answer_input)
        view?.findViewById<Spinner>(R.id.card_type_spinner)!!.adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, CardType.values())
        setOnBackPressedDispatcher()
        if(fragmentMode == EditCardFragmentMode.EDIT_CARD) {
            fillCardInformation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.edit_card_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_card -> {
                showDeleteDialog()
            }
        }
        return true
    }

    private fun showDeleteDialog() {
        val confirmDialog = ConfirmDialog(
            requireContext(),
            getString(R.string.edit_card_delete_card_dialog_title),
            getString(R.string.edit_card_delete_card_dialog_message)
        )
        confirmDialog.setOnConfirmListener {
            cardRepository.deleteCard(card)
            activity?.finish()
        }
        confirmDialog.showDialog()
    }

    private fun setOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })
    }

    fun onBackPressed() {
        val confirmDialog = ConfirmDialog(
            requireContext(),
            getString(R.string.create_card_cancel_dialog_title),
            getString(R.string.create_card_cancel_dialog_message)
        )
        confirmDialog.setOnConfirmListener {
            activity?.finish()
        }
        confirmDialog.showDialog()
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_edit_card)?.setOnClickListener {
            handleFabClicked()
        }
    }

    private fun handleFabClicked() {
        val question = questionInput.text
        val answer = answerInput.text
        when(TextUtils.isEmpty(question) || TextUtils.isEmpty(answer)) {
            true -> {
                handleInvalidInput()
            }
            false -> {
                handleValidInput()
            }
        }
    }

    private fun handleInvalidInput() {
    }

    private fun handleValidInput() {
        when(fragmentMode) {
            EditCardFragmentMode.CREATE_CARD -> {
                saveNewCard()
                requireActivity().finish()
            }
            EditCardFragmentMode.EDIT_CARD -> {
                updateCard()
            }
        }
    }

    private fun updateCard() {
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
            cardRepository.updateCard(updatedCard)
        }
        activity?.finish()
    }

    private fun saveNewCard() {
        // TODO: generic approach
        val questionToken = CardQuestionToken(questionInput.text.toString(), CardQuestionTokenType.TEXT)
        val question = CardQuestion(arrayListOf(questionToken))
        val answer = CardAnswer(arrayListOf(answerInput.text.toString()))
        val card = Card(
            boxId = card.boxId,
            createDate = Date(System.currentTimeMillis()),
            type = CardType.STANDARD,
            question = question,
            answer = answer
        )
        cardRepository.insertCard(card)
    }

    private fun fillCardInformation() {
        questionInput.setText(card.question.getStringRepresentation())
        answerInput.setText(card.answer.getStringRepresentation())
    }

    private fun Bundle.getCardObject(): Card? {
        val parcelableCard = getParcelable<ParcelableCard>(CARD_OBJECT_KEY)
            ?: return null
        return ParcelableCard.createEntity(parcelableCard)
    }

}