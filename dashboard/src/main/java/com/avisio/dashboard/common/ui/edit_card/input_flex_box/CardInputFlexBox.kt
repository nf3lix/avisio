package com.avisio.dashboard.common.ui.edit_card.input_flex_box

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.edit_card.fragment_strategy.CardTypeChangeListener
import com.google.android.flexbox.FlexboxLayout

abstract class CardInputFlexBox(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    companion object {
        const val TEXT_SIZE = 14F
    }

    private val titleTextView: TextView
    private val informationLayout: LinearLayout
    private val informationIcon: ImageView
    private val informationTextView: TextView
    private lateinit var cardTypeChangeListener: CardTypeChangeListener
    val flexbox: FlexboxLayout
    val toolbarContainer: FrameLayout

    init {
        inflate(context, R.layout.card_input_flex_box, this)
        titleTextView = findViewById(R.id.card_input_title_text_view)

        informationLayout = findViewById(R.id.card_input_information_layout)
        informationIcon = findViewById(R.id.card_input_information_icon)
        informationTextView = findViewById(R.id.question_box_information_message)

        flexbox = findViewById(R.id.test_flexbox)
        toolbarContainer = findViewById(R.id.card_input_frame_layout)
    }

    abstract fun initToolbar()

    fun setInformation(message: String, type: CardFlexBoxInformationType = CardFlexBoxInformationType.ERROR) {
        informationLayout.visibility = View.VISIBLE
        informationIcon.setImageResource(type.drawable)
        informationTextView.text = message
        informationTextView.setTextColor(resources.getColor(type.color))
        titleTextView.setTextColor(resources.getColor(type.color))
    }

    fun resetInformation() {
        titleTextView.setTextColor(resources.getColor(R.color.primaryColor))
        informationLayout.visibility = View.GONE
        informationTextView.text = ""
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setCardTypeChangeListener(listener: CardTypeChangeListener) {
        cardTypeChangeListener = listener
    }

}