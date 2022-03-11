package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CardTypeChangeListener
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.MarkdownEditor
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.*
import com.google.android.flexbox.FlexboxLayout

abstract class CardInputFlexBox(context: Context, attributeSet: AttributeSet, val target: TargetInput) : LinearLayout(context, attributeSet) {

    companion object {
        const val TEXT_SIZE = 14F
    }

    private val titleTextView: TextView
    private val informationLayout: LinearLayout
    private val informationIcon: ImageView
    private val informationTextView: TextView
    var currentInformation = CardInputInformation.NONE

    internal var markdown: MarkdownEditor = MarkdownEditor(EditText(context), TextView(context))
    private var markdownDisabled: Boolean = false

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

    fun setInformation(message: String) {
        setInformation(CardInputInformation(message, CardFlexBoxInformationType.INFORMATION))
    }

    fun setSuccess(message: String) {
        setInformation(CardInputInformation(message, CardFlexBoxInformationType.SUCCESS))
    }

    private fun setInformation(information: CardInputInformation) {
        if(information.isBlank()) {
            resetInformation()
            return
        }
        informationLayout.visibility = View.VISIBLE
        informationIcon.setImageResource(information.type.drawable)
        informationIcon.tag = information.type.drawable
        informationTextView.text = information.message
        informationTextView.setTextColor(resources.getColor(information.type.color))
        titleTextView.setTextColor(resources.getColor(information.type.color))
        currentInformation = information
    }

    fun resetInformation() {
        titleTextView.setTextColor(resources.getColor(R.color.primaryColor))
        informationIcon.tag = -1
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

    open fun disableMarkdown() {
        markdownDisabled = true
        markdown.disable()
        resetEditText()
    }

    fun enableMarkdown() {
        markdownDisabled = false
        initMarkdown()
    }

    internal fun initMarkdown() {
        if(markdownDisabled) {
            return
        }
        val blankTextView = TextView(context)
        for(view in flexbox.allViews.toList()) {
            if(view is EditText) {
                val prevText = view.text.toString()
                markdown = MarkdownEditor(view, blankTextView)
                view.setText(prevText)
            }
        }
    }

    abstract fun resetEditText()

}