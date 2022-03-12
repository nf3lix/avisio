package com.avisio.dashboard.usecase.training.activity.question

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardInputFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardInputKeyTextWatcher
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint

class AnswerLearnFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet, SaveCardConstraint.TargetInput.ANSWER_INPUT) {

    companion object {
        private const val MAX_IMAGE_HEIGHT = 300.0
        private const val MAX_IMAGE_WIDTH = 400.0
    }

    private val answerEditText: TextView = TextView(context)
    private var cardAnswer: CardAnswer = CardAnswer(arrayListOf())

    init {
        setTitle(context.getString(R.string.create_card_answer_text_field_hint))
        setMaxCardImageSize(MAX_IMAGE_HEIGHT, MAX_IMAGE_WIDTH)
        disableEditTexts()
    }

    fun setAnswer(answer: CardAnswer) {
        this.cardAnswer = answer
        resetImage()
        setEditTextLayout()
        answerEditText.text = answer.getStringRepresentation()
        if(answer.hasImage()) {
            setImagePath(answer.imagePath!!)
        }
        disableEditTexts()
    }

    fun getAnswer(): String {
        return answerEditText.text.toString()
    }

    override fun initToolbar() {}
    override fun resetEditText() {}

    private fun setEditTextLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            answerEditText.textSize = TEXT_SIZE
        }
        answerEditText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        answerEditText.addTextChangedListener(CardInputKeyTextWatcher(cardChangeListener, this))
        initMarkdown()
        flexbox.addView(answerEditText as View, 0)
    }

    private fun disableEditTexts() {
        for(view in flexbox.allViews.toList()) {
            if(view is EditText) {
                view.isEnabled = false
            }
        }
    }

}