package com.avisio.dashboard.common.ui.edit_card

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.EditCardFragmentStrategy
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.CardTypeChangeListener
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.QuestionFlexBox
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditCardFragment : Fragment(), CardTypeChangeListener {

    companion object {
        const val FRAGMENT_MODE_KEY: String = "EDIT_CARD_FRAGMENT_MODE"
        const val CARD_OBJECT_KEY: String = "CARD_OBJECT"
    }

    private lateinit var questionInput: QuestionFlexBox
    private lateinit var answerInput: AnswerFlexBox

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

    private fun Bundle.getCardObject(): Card? {
        val parcelableCard = getParcelable<ParcelableCard>(CARD_OBJECT_KEY)
            ?: return null
        return ParcelableCard.createEntity(parcelableCard)
    }

    override fun onCardTypeSet(cardType: CardType) {

    }

}