package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.allViews
import androidx.core.view.marginBottom
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.persistence.card.CardImageStorage
import com.avisio.dashboard.common.ui.card_image.CardImage
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.TargetInput.*
import com.google.android.flexbox.FlexboxLayout
import kotlin.math.min
import kotlin.math.roundToInt

class AnswerFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet, ANSWER_INPUT) {

    private val answerEditText: EditText = EditText(context)
    private lateinit var toolbar: CardAnswerInputToolbar
    private var selectImageObserver: SelectAnswerImageObserver? = null

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
        if(flexbox.allViews.toList().size == 6) {
            return CardAnswer(cardAnswer.answerList, (flexbox.allViews.toList()[2].tag as String))
        }
        return cardAnswer
    }

    fun setAnswer(answer: CardAnswer) {
        flexbox.removeAllViews()
        setEditTextLayout()
        answerEditText.setText(answer.getStringRepresentation())
        if(answer.hasImage()) {
            val image = CardImageStorage(context).loadBitmap(answer.imagePath!!)
            val imageView = getImageView(image)
            imageView.tag = answer.imagePath
            flexbox.addView(imageView)
        }
    }

    fun addInitialEditText() {
        setEditTextLayout()
    }

    override fun resetEditText() {
        val cardAnswer = getAnswer()
        flexbox.removeAllViews()
        setEditTextLayout()
        if(cardAnswer.hasImage()) {
            val image = CardImageStorage(context).loadBitmap(cardAnswer.imagePath!!)
            val imageView = getImageView(image)
            imageView.tag = cardAnswer.imagePath
            flexbox.addView(imageView)
        }
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

    fun setSelectImageObserver(selectImageObserver: SelectAnswerImageObserver) {
        this.selectImageObserver = selectImageObserver
    }

    private fun getImageView(bitmap: Bitmap): CardImage {
        val image = CardImage(context)
        image.setImage(bitmap)
        return image
    }

}