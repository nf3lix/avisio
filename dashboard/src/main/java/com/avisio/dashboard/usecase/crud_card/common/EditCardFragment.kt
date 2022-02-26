package com.avisio.dashboard.usecase.crud_card.common

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.CardType.*
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.persistence.card.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CardFragmentStrategy
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CardTypeChangeListener
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardFlexBoxInformationType.*
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardInputFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.SelectImageObserver
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.type_change_handler.CardTypeChangeHandler
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardValidator
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditCardFragment : Fragment(), CardTypeChangeListener, SelectImageObserver {

    companion object {
        const val CARD_CRUD_WORKFLOW: String = "CARD_CRUD_WORKFLOW"
        const val CARD_OBJECT_KEY: String = "CARD_OBJECT"
    }

    lateinit var questionInput: QuestionFlexBox
    lateinit var answerInput: AnswerFlexBox
    lateinit var typeSpinner: Spinner

    private lateinit var card: Card
    internal lateinit var workflow: CRUD
    private lateinit var fragmentStrategy: CardFragmentStrategy
    private lateinit var selectImageObserver: SelectImageResultObserver

    private lateinit var cardRepository: CardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        cardRepository = CardRepository(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            card = it.getCardObject()!!
            workflow = CRUD.values()[it.getInt(CARD_CRUD_WORKFLOW)]
        }
        return inflater.inflate(R.layout.fragment_edit_card, container, false)
    }

    override fun onStart() {
        super.onStart()
        selectImageObserver = SelectImageResultObserver(this, requireActivity().activityResultRegistry)
        lifecycle.addObserver(selectImageObserver)
        setupFab()
        questionInput = requireView().findViewById(R.id.question_flexbox)
        answerInput = requireView().findViewById(R.id.answer_flex_box)
        questionInput.setCardTypeChangeListener(this)
        questionInput.setSelectImageObserver(this)
        answerInput.setCardTypeChangeListener(this)
        answerInput.addInitialEditText()
        initTypeSpinner()
        view?.findViewById<Spinner>(R.id.card_type_spinner)!!.adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, CardType.values())
        fragmentStrategy = CardFragmentStrategy.getStrategy(this, card, cardRepository)
        setOnBackPressedDispatcher()
        setupAppBar()
        fragmentStrategy.fillCardInformation()
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
        val handler = CardTypeChangeHandler.getHandler(this, cardType)
        handler.updateCardInputs()
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

    private fun setupAppBar() {
        val toolbar = requireView().findViewById<Toolbar>(R.id.card_activity_app_bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.title = requireContext().getString(fragmentStrategy.titleId)
        }
        if(workflow == CRUD.UPDATE) {
            toolbar.inflateMenu(R.menu.edit_card_menu)
            toolbar.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.menu_delete_card -> {
                        showDeleteDialog()
                        false
                    } else -> {
                        true
                    }
                }
            }
        }
    }

    override fun onStartSelect() {
        selectImageObserver.startSelectImageActivity()
        // val intent = Intent()
        // intent.type = "image/*"
        // intent.action = Intent.ACTION_GET_CONTENT
        // obs
    }

}