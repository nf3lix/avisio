package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import com.avisio.dashboard.R

class CardQuestionInputToolbar(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    val clozeTextButton: Button

    init {
        inflate(context, R.layout.card_question_input_toolbar, this)
        clozeTextButton = findViewById(R.id.button_add_cloze)
    }

}