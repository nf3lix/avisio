package com.avisio.dashboard.usecase.training.activity

import android.content.Context
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.training.QuestionResult
import com.google.android.material.chip.Chip

class QuestionResultChip(learnCardView: LearnCardView, result: QuestionResult, context: Context) : Chip(context) {

    init {
        inflate(getContext(), R.layout.chip_question_result, null)
        text = context.getString(result.stringRepresentation)
        setOnClickListener {
            result.onOptionSelected(learnCardView)
        }
    }

}