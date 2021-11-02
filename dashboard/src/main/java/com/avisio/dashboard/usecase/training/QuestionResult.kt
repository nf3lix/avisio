package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.training.activity.LearnCardView

enum class QuestionResult(val stringRepresentation: Int) {

    CORRECT(R.string.learn_activity_result_button_correct) {
        override fun onOptionSelected(view: LearnCardView) {
        }
    },

    PARTIALLY_CORRECT(R.string.learn_activity_result_button_partially_correct) {
        override fun onOptionSelected(view: LearnCardView) {
        }
    },

    INCORRECT(R.string.learn_activity_result_button_incorrect) {
        override fun onOptionSelected(view: LearnCardView) {
        }
    };

    abstract fun onOptionSelected(view: LearnCardView)

}