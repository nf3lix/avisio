package com.avisio.dashboard.usecase.training.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.usecase.training.DefaultTrainingStrategy
import com.avisio.dashboard.usecase.training.TrainingStrategy

class LearnBoxFragment : Fragment(), LearnCardView {

    private lateinit var trainingStrategy: TrainingStrategy
    private lateinit var manager: LearnCardManager
    private lateinit var box: AvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            box = it.getBoxObject()!!
        }
    }

    override fun onStart() {
        super.onStart()
        trainingStrategy = DefaultTrainingStrategy(box, requireActivity().application)
        manager = LearnCardManager(this, trainingStrategy)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn_box, container, false)
    }

    override fun showCardQuestion(question: CardQuestion) {
        requireView().findViewById<EditText>(R.id.learn_card_question_edit_text).setText(question.getStringRepresentation())
    }

    override fun onCorrectAnswer() {
        Toast.makeText(context, "correct", Toast.LENGTH_LONG).show()
    }

    override fun onIncorrectAnswer() {
        Toast.makeText(context, "incorrect", Toast.LENGTH_LONG).show()
    }

    override fun onCardLoadFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCardLoading() {
    }

    override fun onCardLoadSuccess(card: Card) {
        requireActivity().runOnUiThread {
            requireView().findViewById<EditText>(R.id.learn_question_edit_text).setText(card.question.getStringRepresentation())
        }
    }

}