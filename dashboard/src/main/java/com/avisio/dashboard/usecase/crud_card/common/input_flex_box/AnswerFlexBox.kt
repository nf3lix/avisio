package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.TargetInput.*

class AnswerFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet, ANSWER_INPUT) {

    private val answerEditText: EditText = EditText(context)

    init {
        setTitle(context.getString(R.string.create_card_answer_text_field_hint))
    }

    override fun initToolbar() {

    }

    fun getAnswer(): CardAnswer {
        return CardAnswer.getFromStringRepresentation(answerEditText.text.toString())
    }

    fun setAnswer(answer: CardAnswer) {
        answerEditText.setText(answer.getStringRepresentation())
    }

    fun addInitialEditText() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            answerEditText.textSize = TEXT_SIZE
        }
        answerEditText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        answerEditText.addTextChangedListener(CardInputKeyTextWatcher(cardChangeListener, this))
        flexbox.addView(answerEditText as View, 0)
    }

}