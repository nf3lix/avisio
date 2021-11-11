package com.avisio.dashboard.common.ui.edit_card.input_flex_box

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.avisio.dashboard.R
import com.google.android.flexbox.FlexboxLayout

abstract class CardInputFlexBox(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    companion object {
        const val TEXT_SIZE = 14F
    }

    private val titleTextView: TextView
    private val errorMessageTextView: TextView
    val flexbox: FlexboxLayout
    val toolbarContainer: FrameLayout

    init {
        inflate(context, R.layout.card_input_flex_box, this)
        titleTextView = findViewById(R.id.card_input_title_text_view)
        errorMessageTextView = findViewById(R.id.question_box_error_message)
        flexbox = findViewById(R.id.test_flexbox)
        toolbarContainer = findViewById(R.id.card_input_frame_layout)
    }

    abstract fun initToolbar()

    fun setError(errorMessage: String) {
        errorMessageTextView.visibility = View.VISIBLE
        errorMessageTextView.text = errorMessage
        titleTextView.setTextColor(resources.getColor(R.color.warning))
    }

    fun resetError() {
        titleTextView.setTextColor(resources.getColor(R.color.primaryColor))
        errorMessageTextView.visibility = View.GONE
        errorMessageTextView.text = ""
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

}