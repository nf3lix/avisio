package com.avisio.dashboard.usecase.training

import com.avisio.dashboard.usecase.training.activity.LearnCardView

enum class QuestionResult {

    CORRECT {
        override fun adaptView(view: LearnCardView) {
            TODO("Not yet implemented")
        }
    },
    PARTIALLY_CORRECT {
        override fun adaptView(view: LearnCardView) {
            TODO("Not yet implemented")
        }
    },
    INCORRECT {
        override fun adaptView(view: LearnCardView) {
            TODO("Not yet implemented")
        }
    };

    abstract fun adaptView(view: LearnCardView)

}