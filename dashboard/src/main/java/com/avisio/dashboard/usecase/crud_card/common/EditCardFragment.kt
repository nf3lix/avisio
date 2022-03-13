package com.avisio.dashboard.usecase.crud_card.common

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.CardType.CLOZE_TEXT
import com.avisio.dashboard.common.data.model.card.CardType.STANDARD
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.persistence.card.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CardFragmentStrategy
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CardTypeChangeListener
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.*
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardFlexBoxInformationType.*
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.type_change_handler.CardTypeChangeHandler
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardValidator
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditCardFragment : Fragment(), CardTypeChangeListener, SelectQuestionImageObserver {

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
    private lateinit var selectQuestionImageObserver: SelectQuestionImageResultObserver
    private lateinit var selectAnswerImageObserver: SelectAnswerImageResultObserver
    private lateinit var deleteQuestionImageObserver: DeleteCardImageObserver.DeleteQuestionImage
    private lateinit var deleteAnswerImageObserver: DeleteCardImageObserver.DeleteAnswerImage
    private lateinit var requestPermissionQuestionImageLauncher: ActivityResultLauncher<String>
    private lateinit var requestPermissionAnswerImageLauncher: ActivityResultLauncher<String>
    private lateinit var cardRepository: CardRepository
    private var fragmentInitialized = false
    private var lastImageSelection = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        cardRepository = CardRepository(requireActivity().application)
        requestPermissionQuestionImageLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
            if(granted) {
                selectQuestionImageObserver.startSelectImageActivity()
            }
        }
        requestPermissionAnswerImageLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
            if(granted) {
                selectAnswerImageObserver.startSelectImageActivity()
            }
        }

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
        selectQuestionImageObserver = SelectQuestionImageResultObserver(this, requireActivity().activityResultRegistry)
        selectAnswerImageObserver = SelectAnswerImageResultObserver(this, requireActivity().activityResultRegistry)
        lifecycle.addObserver(selectQuestionImageObserver)
        lifecycle.addObserver(selectAnswerImageObserver)
        setupFab()
        questionInput = requireView().findViewById(R.id.question_flexbox)
        answerInput = requireView().findViewById(R.id.answer_flex_box)
        questionInput.setCardTypeChangeListener(this)
        questionInput.setSelectImageObserver(this)
        answerInput.setSelectImageObserver(SelectAnswerImageObserver(this, requestPermissionAnswerImageLauncher, selectAnswerImageObserver))
        answerInput.setCardTypeChangeListener(this)
        if(!fragmentInitialized) {
            answerInput.addInitialEditText()
        }
        initTypeSpinner()
        view?.findViewById<Spinner>(R.id.card_type_spinner)!!.adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, CardType.displayNames(requireContext()))
        fragmentStrategy = CardFragmentStrategy.getStrategy(this, card, cardRepository)
        setOnBackPressedDispatcher()
        setupAppBar()
        if(!fragmentInitialized) {
            fragmentStrategy.fillCardInformation()
        }
        deleteQuestionImageObserver = DeleteCardImageObserver.DeleteQuestionImage(questionInput)
        deleteAnswerImageObserver = DeleteCardImageObserver.DeleteAnswerImage(answerInput)
        questionInput.setDeleteImageClickListener(deleteQuestionImageObserver)
        answerInput.setDeleteImageClickListener(deleteAnswerImageObserver)
        fragmentInitialized = true
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
        val fragment = this
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val cardQuestion = questionInput.getCardQuestion().tokenList
                val cardAnswer = answerInput.getAnswer()
                if(System.currentTimeMillis() - lastImageSelection > 500) {
                    val selectedType = getSelectedCardType()
                    if(selectedType != STANDARD && (cardQuestion[cardQuestion.size - 1].tokenType == QuestionTokenType.IMAGE || cardAnswer.hasImage())) {
                        DeleteImagesConfirmDialog.showDialog(fragment)
                    } else {
                        onCardTypeSet(selectedType)
                    }
                } else {
                    onCardTypeSet(STANDARD)
                }
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
        return CardType.valueWithDisplayName(typeSpinner.selectedItem.toString(), requireContext())!!
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
        when {
            ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED -> {
                selectQuestionImageObserver.startSelectImageActivity()
            }
            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(READ_EXTERNAL_STORAGE), 1)
            }
            else -> {
                requestPermissionQuestionImageLauncher.launch(READ_EXTERNAL_STORAGE)
            }
        }
    }

    fun questionImageSelected(imagePath: String) {
        val newTokens = questionInput.getCardQuestion(trimmed = false).tokenList
        if(newTokens[newTokens.size - 1].tokenType == QuestionTokenType.IMAGE) {
            newTokens.removeAt(newTokens.size - 1)
            newTokens.add(QuestionToken(imagePath, QuestionTokenType.IMAGE))
            val newCard = CardQuestion(newTokens)
            questionInput.setCardQuestion(newCard)
        } else {
            newTokens.add(QuestionToken(
                content = imagePath,
                tokenType = QuestionTokenType.IMAGE
            ))
            questionInput.setCardQuestion(CardQuestion(newTokens))
        }
        onCardTypeSet(STANDARD)
        lastImageSelection = System.currentTimeMillis()
    }

    fun answerImageSelected(imagePath: String) {
        val prevAnswer = answerInput.getAnswer()
        val newAnswer = CardAnswer(prevAnswer.answerList, imagePath)
        answerInput.setAnswer(newAnswer)
        onCardTypeSet(STANDARD)
        lastImageSelection = System.currentTimeMillis()
    }

    fun removeAllImages() {
        deleteQuestionImageObserver.onClick()
        deleteAnswerImageObserver.onClick()
    }

}