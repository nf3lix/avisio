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
import com.avisio.dashboard.usecase.training.TrainingStrategy
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class LearnBoxFragment : Fragment(), LearnCardView {

    private lateinit var trainingStrategy: TrainingStrategy
    private lateinit var manager: LearnCardManager
    private lateinit var box: AvisioBox
    private lateinit var currentCard: Card

    private lateinit var correctAnswerLayoutInput: TextInputLayout
    private lateinit var correctAnswerEditText: EditText
    private lateinit var resolveQuestionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            box = it.getBoxObject()!!
        }
    }

    override fun onStart() {
        super.onStart()
        correctAnswerLayoutInput = requireView().findViewById(R.id.correct_answer_input_layout)
        correctAnswerEditText = requireView().findViewById(R.id.correct_answer_edit_text)
        resolveQuestionButton = requireView().findViewById(R.id.resolve_question_button)
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
        Toast.makeText(context, "correct", Toast.LENGTH_LONG).show()
    }

    override fun onIncorrectAnswer() {
        Toast.makeText(context, "incorrect", Toast.LENGTH_LONG).show()
        correctAnswerLayoutInput.visibility = View.VISIBLE
        resolveQuestionButton.visibility = View.GONE
        requireView().findViewById<ChipGroup>(R.id.chipGroup).visibility = View.VISIBLE
        hideResolveQuestionButton()
        correctAnswerEditText.setText(currentCard.answer.getStringRepresentation())
    }

    private fun hideResolveQuestionButton() {
        val constraintLayout = requireView().findViewById<ConstraintLayout>(R.id.learn_card_constraint_layout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(correctAnswerLayoutInput.id, ConstraintSet.TOP, R.id.answer_input_layout, ConstraintSet.TOP, 0)
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

}