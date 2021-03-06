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
    private lateinit var toolbar: CardAnswerInputToolbar
    private var selectImageObserver: SelectImageObserver? = null

    init {
        setTitle(context.getString(R.string.create_card_answer_text_field_hint))
        initToolbar()
    }

    override fun initToolbar() {
        toolbar = CardAnswerInputToolbar(context)
        toolbarContainer.addView(toolbar as View)
        toolbar.selectImageButton.setOnClickListener {
            selectImageObserver?.onStartSelect()
        }
    }

    fun getAnswer(): CardAnswer {
        val cardAnswer = CardAnswer.getFromStringRepresentation(answerEditText.text.toString())
        if(hasImage()) {
            return CardAnswer(cardAnswer.answerList, getImagePath())
        }
        return cardAnswer
    }

    fun setAnswer(answer: CardAnswer) {
        flexbox.removeAllViews()
        resetImage()
        setEditTextLayout()
        answerEditText.setText(answer.getStringRepresentation())
        if(answer.hasImage()) {
            setImagePath(answer.imagePath!!)
        }
    }

    fun addInitialEditText() {
        setEditTextLayout()
    }

    override fun resetEditText() {
        flexbox.removeAllViews()
        setEditTextLayout()
    }

    private fun setEditTextLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            answerEditText.textSize = TEXT_SIZE
        }
        answerEditText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        answerEditText.addTextChangedListener(CardInputKeyTextWatcher(cardChangeListener, this))
        initMarkdown()
        flexbox.addView(answerEditText as View, 0)
    }

    fun setSelectImageObserver(selectImageObserver: SelectImageObserver) {
        this.selectImageObserver = selectImageObserver
    }

}