package com.avisio.dashboard.usecase.training.activity.question

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.CardInputFlexBox
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.QuestionFlexBox
import com.google.android.material.chip.Chip
import java.lang.StringBuilder

class QuestionLearnFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet) {

    init {
    }

    fun setQuestion(cardQuestion: CardQuestion) {
        flexbox.removeAllViews()
        for((index, token) in cardQuestion.tokenList.withIndex()) {
            when(token.tokenType) {
                CardQuestionTokenType.TEXT -> {
                    val editText = getEditText(token.content.trim())
                    flexbox.addView(editText, index)
                }
                CardQuestionTokenType.QUESTION -> {
                    val chip = getClozeChip(getQuestionPlaceholder(token.content.trim()))
                    flexbox.addView(chip, index)
                }
            }
        }
    }

    override fun initToolbar() {
    }

    private fun getQuestionPlaceholder(question: String): String {
        val stringBuilder = StringBuilder()
        for(i in 0..question.length) {
            stringBuilder.append(" ")
        }
        return stringBuilder.toString()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getClozeChip(question: String): Chip {
        val chip = Chip(context)
        chip.text = question
        chip.setChipBackgroundColorResource(R.color.primaryDarkColor)
        chip.textSize = QuestionFlexBox.TEXT_SIZE
        chip.tag = false
        chip.setOnTouchListener{ view, motionEvent ->
            true
        }
        chip.isCheckable = false
        return chip
    }

    private fun getEditText(input: String): EditText {
        val editText = EditText(context)
        editText.setText(input)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.textSize = QuestionFlexBox.TEXT_SIZE
        }
        return editText
    }

}