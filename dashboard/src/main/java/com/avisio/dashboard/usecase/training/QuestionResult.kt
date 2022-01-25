package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.training.activity.LearnCardView

enum class QuestionResult(val grade: Int, val stringRepresentation: Int, val color: Int) {

    WRONG(2, R.string.learn_activity_result_hard, R.color.hard) {
        override fun onOptionSelected(view: LearnCardView) {
            view.onResultOptionSelected(this)
        }
    },

    HARD(3, R.string.learn_activity_result_medium, R.color.medium) {
        override fun onOptionSelected(view: LearnCardView) {
            view.onResultOptionSelected(this)
        }
    },

    MEDIUM(4, R.string.learn_activity_result_ok, R.color.warning) {
        override fun onOptionSelected(view: LearnCardView) {
            view.onResultOptionSelected(this)
        }
    },

    EASY(5, R.string.learn_activity_result_easy, R.color.success) {
        override fun onOptionSelected(view: LearnCardView) {
            view.onResultOptionSelected(this)
        }
    };

    abstract fun onOptionSelected(view: LearnCardView)

}