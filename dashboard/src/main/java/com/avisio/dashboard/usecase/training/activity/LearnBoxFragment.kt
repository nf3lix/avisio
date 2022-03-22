package com.avisio.dashboard.usecase.training.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.usecase.training.QuestionResult
import com.avisio.dashboard.usecase.training.SM15TrainingStrategy
import com.avisio.dashboard.usecase.training.TrainingStrategy
import com.avisio.dashboard.usecase.training.activity.card_type_strategy.CardTypeLayoutStrategy
import com.avisio.dashboard.usecase.training.activity.card_type_strategy.ClozeTextLayoutStrategy
import com.avisio.dashboard.usecase.training.activity.question.QuestionLearnFlexBox
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import io.noties.markwon.Markwon

class LearnBoxFragment : Fragment(), LearnCardView {

    private lateinit var trainingStrategy: TrainingStrategy
    private lateinit var cardTypeLayoutStrategy: CardTypeLayoutStrategy
    private lateinit var manager: LearnCardManager
    private lateinit var box: AvisioBox
    //private
    lateinit var currentCard: Card

    lateinit var questionInputLayout: QuestionLearnFlexBox
    lateinit var answerInputLayout: TextInputLayout
    lateinit var progressBar: ProgressBar
    private lateinit var correctAnswerLayoutInput: TextInputLayout
    lateinit var answerEditText: EditText
    lateinit var correctAnswerEditText: EditText
    private lateinit var resolveQuestionButton: Button
    private lateinit var resultChipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            box = it.getBoxObject()!!
        }
    }

    override fun onStart() {
        super.onStart()
        cardTypeLayoutStrategy = ClozeTextLayoutStrategy(this)
        questionInputLayout = requireView().findViewById(R.id.question_input_layout)
        answerInputLayout = requireView().findViewById(R.id.answer_input_layout)
        answerEditText = requireView().findViewById(R.id.answer_edit_text)
        correctAnswerLayoutInput = requireView().findViewById(R.id.correct_answer_input_layout)
        correctAnswerEditText = requireView().findViewById(R.id.correct_answer_edit_text)
        resolveQuestionButton = requireView().findViewById(R.id.resolve_question_button)
        progressBar = requireView().findViewById(R.id.load_card_progressBar)
        resultChipGroup = requireView().findViewById(R.id.chipGroup)
        addAllQuestionResultChips()
        trainingStrategy = SM15TrainingStrategy(box, requireActivity().application)
        manager = LearnCardManager(this, trainingStrategy)
        trainingStrategy.initObserver(manager)
        setupFab()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn_box, container, false)
    }

    override fun onCardLoadSuccess(card: Card) {
        enableButtons()
        hideProgressBar()
        showAnswerEditText()
        currentCard = card
        cardTypeLayoutStrategy = CardTypeLayoutStrategy.getCardTypeStrategy(currentCard, this)
        try {
            requireActivity().runOnUiThread {
                requireView().findViewById<QuestionLearnFlexBox>(R.id.question_input_layout).setQuestion(currentCard.question)
                showResolveQuestionButton()
                answerInputLayout.visibility = View.VISIBLE
                resultChipGroup.visibility = View.GONE
                correctAnswerLayoutInput.visibility = View.GONE
                cardTypeLayoutStrategy.onShowCard()
            }
        } catch (ignore: IllegalStateException) { }
    }

    override fun onCorrectAnswer() {
        correctAnswerLayoutInput.visibility = View.VISIBLE
        resultChipGroup.visibility = View.VISIBLE
        hideResolveQuestionButton()
        correctAnswerEditText.setText(currentCard.answer.getStringRepresentation())
        cardTypeLayoutStrategy.onCorrectAnswer()
    }

    override fun onIncorrectAnswer() {
        correctAnswerLayoutInput.visibility = View.VISIBLE
        resultChipGroup.visibility = View.VISIBLE
        hideResolveQuestionButton()
        correctAnswerEditText.setText(currentCard.answer.getStringRepresentation())
        cardTypeLayoutStrategy.onIncorrectAnswer()

    }

    override fun onPartiallyCorrectAnswer() {
        resultChipGroup.visibility = View.VISIBLE
        hideResolveQuestionButton()
        cardTypeLayoutStrategy.onPartiallyCorrectAnswer()
    }

    private fun hideResolveQuestionButton() {
        resolveQuestionButton.visibility = View.GONE
        answerInputLayout.visibility = View.GONE
        val constraintLayout = requireView().findViewById<ConstraintLayout>(R.id.learn_card_constraint_layout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(correctAnswerLayoutInput.id, ConstraintSet.TOP, R.id.question_input_layout, ConstraintSet.TOP, 0)
        constraintSet.applyTo(constraintLayout)
    }

    private fun showResolveQuestionButton() {
        resolveQuestionButton.visibility = View.VISIBLE
        val constraintLayout = requireView().findViewById<ConstraintLayout>(R.id.learn_card_constraint_layout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(resolveQuestionButton.id, ConstraintSet.TOP, R.id.answer_input_layout, ConstraintSet.TOP, 0)
        constraintSet.connect(correctAnswerLayoutInput.id, ConstraintSet.TOP, resolveQuestionButton.id, ConstraintSet.TOP, 0)
        constraintSet.applyTo(constraintLayout)
    }

    override fun onCardLoadFailure(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCardLoading() {
        disableButtons()
        showProgressBar()
        hideAnswerEditText()
    }

    private fun setupFab() {
        resolveQuestionButton.setOnClickListener {
            manager.onAnswer(cardTypeLayoutStrategy.getQuestionResult(currentCard, cardTypeLayoutStrategy.getUserInputAsAnswer()))
        }
    }

    fun showStandardAnswerTextView() {
        val textView = requireView().findViewById<TextView>(R.id.standard_answer_text_view)
        textView.visibility = View.VISIBLE
        textView.text = currentCard.answer.getStringRepresentation()
        MarkdownView.enableMarkdown(Markwon.create(requireContext()), textView)
    }

    fun hideStandardAnswerTextView() {
        val textView = requireView().findViewById<TextView>(R.id.standard_answer_text_view)
        answerEditText.visibility = View.VISIBLE
        textView.text = ""
        textView.visibility = View.GONE
    }

    fun showAnswerEditText() {
        try {
            requireActivity().runOnUiThread {
                answerInputLayout.visibility = View.VISIBLE
            }
        } catch (ignore: IllegalStateException) { }
    }

    fun hideAnswerEditText() {
        try {
            requireActivity().runOnUiThread {
                answerInputLayout.visibility = View.GONE
            }
        } catch (ignore: IllegalStateException) { }
    }

    private fun addAllQuestionResultChips() {
        for(questionResult in QuestionResult.values()) {
            resultChipGroup.addView(QuestionResultChip(this, questionResult, requireContext(), null))
        }
    }

    override fun onResultOptionSelected(result: QuestionResult) {
        cardTypeLayoutStrategy.resetCard()
        manager.onResultOptionSelected(result)
    }

    override fun onTrainingFinished() {
        requireActivity().runOnUiThread {
            Toast.makeText(context, R.string.learn_activity_training_finished, Toast.LENGTH_LONG).show()
            questionInputLayout.visibility = View.GONE
            answerInputLayout.visibility = View.GONE
            correctAnswerLayoutInput.visibility = View.GONE
            resultChipGroup.visibility = View.GONE
            resolveQuestionButton.visibility = View.GONE
            activity?.onBackPressed()
        }
    }

    private fun disableButtons() {
        try {
            requireActivity().runOnUiThread {
                resolveQuestionButton.isEnabled = false
            }
        } catch (ignore: IllegalStateException) { }
    }

    private fun enableButtons() {
        try {
            requireActivity().runOnUiThread {
                resolveQuestionButton.isEnabled = true
            }
        } catch (ignore: IllegalStateException) { }
    }

    private fun hideProgressBar() {
        try {
            requireActivity().runOnUiThread {
                progressBar.visibility = View.GONE
            }
        } catch (ignore: IllegalStateException) { }
    }

    private fun showProgressBar() {
        try {
            requireActivity().runOnUiThread {
                progressBar.visibility = View.VISIBLE
            }
        } catch (ignore: IllegalStateException) { }
    }

}