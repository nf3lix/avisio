package com.avisio.dashboard.usecase.crud_card.common

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.CardType.*
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CardTypeChangeListener
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.EditCardFragmentStrategy
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardFlexBoxInformationType.*
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardInputFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardValidator
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditCardFragment : Fragment(), CardTypeChangeListener {

    companion object {
        const val FRAGMENT_MODE_KEY: String = "EDIT_CARD_FRAGMENT_MODE"
        const val CARD_OBJECT_KEY: String = "CARD_OBJECT"
    }

    lateinit var questionInput: QuestionFlexBox
    lateinit var answerInput: AnswerFlexBox
    lateinit var typeSpinner: Spinner

    private lateinit var card: Card
    private lateinit var fragmentMode: EditCardFragmentMode
    private lateinit var fragmentStrategy: EditCardFragmentStrategy

    private lateinit var cardRepository: CardRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        cardRepository = CardRepository(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            card = it.getCardObject()!!
            fragmentMode = EditCardFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
        }
        return inflater.inflate(R.layout.fragment_edit_card, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupFab()
        questionInput = requireView().findViewById(R.id.question_flexbox)
        answerInput = requireView().findViewById(R.id.answer_flex_box)
        questionInput.setCardTypeChangeListener(this)
        answerInput.setCardTypeChangeListener(this)
        answerInput.addInitialEditText()
        initTypeSpinner()
        view?.findViewById<Spinner>(R.id.card_type_spinner)!!.adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, CardType.values())
        fragmentStrategy = fragmentMode.getFragmentStrategy(this, card, cardRepository)
        setOnBackPressedDispatcher()
        fragmentStrategy.fillCardInformation()
        if(fragmentMode == EditCardFragmentMode.EDIT_CARD) {
            questionInput.setCardQuestion(card.question)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(fragmentMode == EditCardFragmentMode.EDIT_CARD) {
            requireActivity().menuInflater.inflate(R.menu.edit_card_menu, menu)
        }
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
                fragmentStrategy.onBackPressed()
            }
        })
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_edit_card)?.setOnClickListener {
            fragmentStrategy.onFabClicked()
        }
    }

    private fun initTypeSpinner() {
        typeSpinner = requireView().findViewById(R.id.card_type_spinner)
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                onCardTypeSet(getSelectedCardType())
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun Bundle.getCardObject(): Card? {
        val parcelableCard = getParcelable<ParcelableCard>(CARD_OBJECT_KEY)
            ?: return null
        return ParcelableCard.createEntity(parcelableCard)
    }

    override fun onCardTypeSet(cardType: CardType) {
        if(cardType == CLOZE_TEXT) {
            answerInput.isEnabled = false
            if(!answerInput.getAnswer().answerIsEmpty()) {
                answerInput.setWarning(requireContext().getString(R.string.edit_card_cloze_text_answer_is_ignored))
            } else {
                answerInput.visibility = View.GONE
            }
        }
        if(cardType == STANDARD) {
            questionInput.replaceClozeTextByStandardQuestion()
        }
        if(cardType == STANDARD || cardType == CUSTOM) {
            answerInput.resetInformation()
            answerInput.visibility = View.VISIBLE
            answerInput.isEnabled = true
        }
        typeSpinner.setSelection(cardType.ordinal)
        onFlexboxInputChanged(questionInput)
        onFlexboxInputChanged(answerInput)
    }

    override fun onFlexboxInputChanged(input: CardInputFlexBox) {
        val previousInformation = input.currentInformation
        if(previousInformation.type == ERROR) {
            input.resetInformation()
        }
        if(allConstraintsFulfilled(input)) {
            if(previousInformation.type != SUCCESS && !(getSelectedCardType() == CLOZE_TEXT && input is AnswerFlexBox)) {
                input.setSuccess("")
            }
        } else if(previousInformation.type != WARNING) {
            input.resetInformation()
        }
    }

    private fun allConstraintsFulfilled(input: CardInputFlexBox): Boolean {
        return getUnfulfilledConstraints(input).isEmpty()
    }

    private fun getUnfulfilledConstraints(input: CardInputFlexBox): List<SaveCardConstraint> {
        return SaveCardValidator.getTargetSpecificUnfulfilledConstraints(
            Card(answer = answerInput.getAnswer(), question = questionInput.getCardQuestion(), type = getSelectedCardType()),
            input.target)
    }

    fun getSelectedCardType(): CardType {
        return CardType.valueOf(typeSpinner.selectedItem.toString())
    }

}