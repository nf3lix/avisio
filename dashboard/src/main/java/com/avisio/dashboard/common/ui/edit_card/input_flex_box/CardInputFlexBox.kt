package com.avisio.dashboard.common.ui.edit_card.input_flex_box

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
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
    var currentInformation = CardInputInformation.NONE

    var cardChangeListener: CardTypeChangeListener = EditCardFragment()
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

    fun setError(message: String) {
        setInformation(CardInputInformation(message, CardFlexBoxInformationType.ERROR))
    }

    fun setWarning(message: String) {
        setInformation(CardInputInformation(message, CardFlexBoxInformationType.WARNING))
    }

    private fun setInformation(information: CardInputInformation) {
        if(information.isBlank()) {
            resetInformation()
            return
        }
        informationLayout.visibility = View.VISIBLE
        informationIcon.setImageResource(information.type.drawable)
        informationTextView.text = information.message
        informationTextView.setTextColor(resources.getColor(information.type.color))
        titleTextView.setTextColor(resources.getColor(information.type.color))
        currentInformation = information
    }

    fun resetInformation() {
        titleTextView.setTextColor(resources.getColor(R.color.primaryColor))
        informationLayout.visibility = View.GONE
        informationTextView.text = ""
        currentInformation = CardInputInformation.NONE
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setCardTypeChangeListener(listener: CardTypeChangeListener) {
        cardChangeListener = listener
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        for(view in flexbox.allViews.toList()) {
            view.isEnabled = enabled
        }
    }

}