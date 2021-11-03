package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.training.activity.LearnCardView

enum class QuestionResult(val stringRepresentation: Int, val color: Int) {

    CORRECT(R.string.learn_activity_result_button_correct, R.color.correctAnswer) {
        override fun onOptionSelected(view: LearnCardView) {
            view.onResultOptionSelected(this)
        }
    },

    PARTIALLY_CORRECT(R.string.learn_activity_result_button_partially_correct, R.color.partiallyCorrectAnswer) {
        override fun onOptionSelected(view: LearnCardView) {
            view.onResultOptionSelected(this)
        }
    },

    INCORRECT(R.string.learn_activity_result_button_incorrect, R.color.incorrectAnswer) {
        override fun onOptionSelected(view: LearnCardView) {
            view.onResultOptionSelected(this)
        }
    };

    abstract fun onOptionSelected(view: LearnCardView)

}