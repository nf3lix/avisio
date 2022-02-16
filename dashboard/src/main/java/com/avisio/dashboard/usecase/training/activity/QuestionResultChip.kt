package com.avisio.dashboard.usecase.training.activity

import android.content.Context
import android.util.AttributeSet
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.training.QuestionResult
import com.google.android.material.chip.Chip
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuestionResultChip(learnCardView: LearnCardView, private val result: QuestionResult, context: Context, attributeSet: AttributeSet?) : Chip(context, attributeSet) {

    init {
        inflate(getContext(), R.layout.chip_question_result, null)
        text = context.getString(result.stringRepresentation)
        setOnClickListener {
            result.onOptionSelected(learnCardView)
        }
    }

}