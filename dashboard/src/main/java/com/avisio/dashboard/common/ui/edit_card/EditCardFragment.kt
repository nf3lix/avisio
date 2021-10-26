package com.avisio.dashboard.common.ui.edit_card

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.*
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class EditCardFragment : Fragment() {

    companion object {
        const val FRAGMENT_MODE_KEY: String = "EDIT_CARD_FRAGMENT_MODE"
        const val BOX_OBJECT_KEY: String = "EDIT_CARD_BOX_OBJECT"
    }

    private lateinit var questionInput: AppCompatEditText
    private lateinit var answerInput: AppCompatEditText

    private var fragmentMode: EditCardFragmentMode = EditCardFragmentMode.CREATE_CARD
    private lateinit var parcelableBox: ParcelableAvisioBox

    private lateinit var cardRepository: CardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardRepository = CardRepository(requireActivity().application)
        arguments?.let {
            fragmentMode = EditCardFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
            parcelableBox = it.getParcelable(BOX_OBJECT_KEY)!!
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
            }
        }
    }

    private fun saveNewCard() {
        // TODO: generic approach
        val questionToken = CardQuestionToken(questionInput.text.toString(), CardQuestionTokenType.TEXT)
        val question = CardQuestion(arrayListOf(questionToken))
        val answer = CardAnswer(arrayListOf(answerInput.text.toString()))
        val card = Card(
            boxId = parcelableBox.boxId,
            createDate = Date(System.currentTimeMillis()),
            type = CardType.STANDARD,
            question = question,
            answer = answer
        )
        cardRepository.insertCard(card)
    }


}