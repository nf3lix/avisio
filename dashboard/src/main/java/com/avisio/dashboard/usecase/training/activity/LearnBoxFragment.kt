package com.avisio.dashboard.usecase.training.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.usecase.training.DefaultTrainingStrategy
import com.avisio.dashboard.usecase.training.QuestionResult
import com.avisio.dashboard.usecase.training.TrainingStrategy
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class LearnBoxFragment : Fragment(), LearnCardView {

    private lateinit var trainingStrategy: TrainingStrategy
    private lateinit var manager: LearnCardManager
    private lateinit var box: AvisioBox
    private lateinit var currentCard: Card

    private lateinit var answerInputLayout: TextInputLayout
    private lateinit var correctAnswerLayoutInput: TextInputLayout
    private lateinit var correctAnswerEditText: EditText
    private lateinit var resolveQuestionButton: Button
    private lateinit var resultChipGroup: ChipGroup

    private lateinit var correctChip: QuestionResultChip
    private lateinit var partiallyChip: QuestionResultChip
    private lateinit var incorrectChip: QuestionResultChip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            box = it.getBoxObject()!!
        }
    }

    override fun onStart() {
        super.onStart()
        answerInputLayout = requireView().findViewById(R.id.answer_input_layout)
        correctAnswerLayoutInput = requireView().findViewById(R.id.correct_answer_input_layout)
        correctAnswerEditText = requireView().findViewById(R.id.correct_answer_edit_text)
        resolveQuestionButton = requireView().findViewById(R.id.resolve_question_button)
        resultChipGroup = requireView().findViewById(R.id.chipGroup)
        setupResultChipGroup()
        trainingStrategy = DefaultTrainingStrategy(box, requireActivity().application)
        manager = LearnCardManager(this, trainingStrategy)
        setupFab()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn_box, container, false)
    }

    override fun showCard(card: Card) {
        currentCard = card
        requireActivity().runOnUiThread {
            requireView().findViewById<EditText>(R.id.question_edit_text).setText(currentCard.question.getStringRepresentation())
        }
    }

    override fun onCorrectAnswer() {
        resultChipGroup.visibility = View.VISIBLE
        hideResolveQuestionButton()
        correctChip.setSuggestedResult()
    }

    override fun onIncorrectAnswer() {
        correctAnswerLayoutInput.visibility = View.VISIBLE
        resultChipGroup.visibility = View.VISIBLE
        hideResolveQuestionButton()
        correctAnswerEditText.setText(currentCard.answer.getStringRepresentation())
        incorrectChip.setSuggestedResult()
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
        constraintSet.setVerticalBias(resolveQuestionButton.id, 1.220F)
        constraintSet.applyTo(constraintLayout)
    }

    override fun onCardLoadFailure(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCardLoading() {
    }

    private fun setupFab() {
        resolveQuestionButton.setOnClickListener {
            manager.onAnswer(requireView().findViewById<EditText>(R.id.answer_edit_text).text.toString())
        }
    }

    private fun setupResultChipGroup() {
        correctChip = QuestionResultChip(this, QuestionResult.CORRECT, requireContext(), null)
        partiallyChip = QuestionResultChip(this, QuestionResult.PARTIALLY_CORRECT, requireContext(), null)
        incorrectChip = QuestionResultChip(this, QuestionResult.INCORRECT, requireContext(), null)
        resultChipGroup.addView(correctChip)
        resultChipGroup.addView(partiallyChip)
        resultChipGroup.addView(incorrectChip)
    }

    override fun onResultOptionSelected(result: QuestionResult) {
        manager.onResultOptionSelected(result)
        answerInputLayout.visibility = View.VISIBLE
        resultChipGroup.visibility = View.GONE
        correctAnswerLayoutInput.visibility = View.GONE
        showResolveQuestionButton()
    }

    override fun onTrainingFinished() {
        requireActivity().runOnUiThread {
            Toast.makeText(context, R.string.learn_activity_training_finished, Toast.LENGTH_LONG).show()
        }
    }

}