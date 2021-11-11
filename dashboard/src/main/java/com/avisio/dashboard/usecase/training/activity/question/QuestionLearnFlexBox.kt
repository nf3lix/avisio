package com.avisio.dashboard.usecase.training.activity.question

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.ui.InputDialog
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.CardInputFlexBox
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.QuestionFlexBox
import com.google.android.material.chip.Chip

class QuestionLearnFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet) {

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
        removeEmptyEditTexts()
        disableEditTexts()
    }

    private fun removeEmptyEditTexts() {
        for(view in flexbox.allViews.toList()) {
            if(view is EditText && view.text.toString().isEmpty()) {
                flexbox.removeView(view)
            }
        }
    }

    private fun disableEditTexts() {
        for(view in flexbox.allViews.toList()) {
            if(view is EditText) {
                view.isEnabled = false
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
        chip.setOnClickListener {
            InputDialog(this, chip, rootView as? ViewGroup).showDialog()
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