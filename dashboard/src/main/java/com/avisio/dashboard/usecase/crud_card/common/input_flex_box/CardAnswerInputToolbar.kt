package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import com.avisio.dashboard.R

class CardAnswerInputToolbar(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    val selectImageButton: Button

    init {
        inflate(context, R.layout.card_answer_input_toolbar, this)
        selectImageButton = findViewById(R.id.button_select_answer_image)
    }

}