package com.avisio.dashboard.usecase.training.activity

import android.content.Context
import android.util.AttributeSet
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.training.QuestionResult
import com.google.android.material.chip.Chip

class QuestionResultChip(learnCardView: LearnCardView, private val result: QuestionResult, context: Context, attributeSet: AttributeSet?) : Chip(context, attributeSet) {

    init {
        inflate(getContext(), R.layout.chip_question_result, null)
        text = context.getString(result.stringRepresentation)
        setOnClickListener {
            clearSuggestedResult()
            result.onOptionSelected(learnCardView)
        }
    }

    fun setSuggestedResult() {
        setChipBackgroundColorResource(result.color)
    }

    private fun clearSuggestedResult() {
        setChipBackgroundColorResource(R.color.textColor)
    }

}