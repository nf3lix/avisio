package com.avisio.dashboard.usecase.training.activity.question

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.ui.InputDialog
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardInputFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.TargetInput.*
import com.avisio.dashboard.usecase.training.activity.MarkdownView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import io.noties.markwon.Markwon
import kotlin.math.min
import kotlin.math.roundToInt

class QuestionLearnFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet, QUESTION_INPUT) {

    companion object {
        private const val MAX_IMAGE_HEIGHT = 300.0
        private const val MAX_IMAGE_WIDTH = 400.0
    }

    private var cardQuestion: CardQuestion = CardQuestion(arrayListOf())
    private val markwon = Markwon.create(context)

    fun setQuestion(cardQuestion: CardQuestion) {
        this.cardQuestion = cardQuestion
        flexbox.removeAllViews()
        for((index, token) in cardQuestion.tokenList.withIndex()) {
            when(token.tokenType) {
                QuestionTokenType.TEXT -> {
                    val textView = getTextView(token.content.trim())
                    flexbox.addView(textView, index)
                    MarkdownView.enableMarkdown(markwon, textView)
                }
                QuestionTokenType.QUESTION -> {
                    val chip = getClozeChip(getQuestionPlaceholder(token.content.trim()))
                    flexbox.addView(chip, index)
                }
                QuestionTokenType.IMAGE -> {
                    val byteArray = context.openFileInput(token.content).readBytes()
                    val decodedImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    val imageView = getImageView(decodedImage)
                    flexbox.addView(imageView)
                }
            }
        }
        removeEmptyEditTexts()
        disableEditTexts()
    }

    fun getQuestion(): CardQuestion {
        return cardQuestion
    }

    fun getAnswer(): CardAnswer {
        val answerTokenList = arrayListOf<String>()
        for(view in flexbox.allViews.toList()) {
            if(view is Chip) {
                answerTokenList.add(view.text.toString())
            }
        }
        return CardAnswer(answerTokenList)
    }

    private fun removeEmptyEditTexts() {
        for(view in flexbox.allViews.toList()) {
            if(view is EditText && view.text.toString().isEmpty()) {
                flexbox.removeView(view)
            }
        }
    }

    private fun disableEditTexts() {
        for(view in flexbox.allViews.toList()) {
            if(view is EditText) {
                view.isEnabled = false
            }
        }
    }

    override fun initToolbar() { }
    override fun resetEditText() { }

    fun getQuestionPlaceholder(question: String): String {
        val stringBuilder = StringBuilder()
        for(i in 0..question.length) {
            stringBuilder.append(" ")
        }
        return stringBuilder.toString()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getClozeChip(question: String): Chip {
        val chip = Chip(context)
        chip.text = question
        chip.setChipBackgroundColorResource(R.color.primaryDarkColor)
        chip.textSize = QuestionFlexBox.TEXT_SIZE
        chip.tag = false
        chip.setOnClickListener {
            InputDialog(this, chip, rootView as? ViewGroup).showDialog()
        }
        chip.isCheckable = false
        return chip
    }

    private fun getTextView(input: String): TextView {
        val textView = TextView(context)
        textView.text = input
        textView.gravity = Gravity.CENTER_VERTICAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.textSize = QuestionFlexBox.TEXT_SIZE
        }
        return textView
    }

    fun correctClozeText() {
        val cardQuestionTokenList = arrayListOf<QuestionToken>()
        for(questionToken in cardQuestion.tokenList) {
            if(questionToken.tokenType == QuestionTokenType.QUESTION) {
                cardQuestionTokenList.add(questionToken)
            }
        }
        val answerTokenList = getAnswer().answerList
        for((index, answerToken) in answerTokenList.withIndex()) {
            getClozeTextChips()[index].isClickable = false
            if(answerToken == cardQuestionTokenList[index].content) {
                getClozeTextChips()[index].setChipBackgroundColorResource(R.color.success)
                continue
            }
            getClozeTextChips()[index].setChipBackgroundColorResource(R.color.error)
        }
    }

    private fun getClozeTextChips(): List<Chip> {
        val chipList = arrayListOf<Chip>()
        for(view in flexbox.allViews.toList()) {
            if(view is Chip) {
                chipList.add(view)
            }
        }
        return chipList
    }

    private fun getImageView(bitmap: Bitmap): ImageView {
        val imageView = ImageView(context)
        val scale = min((MAX_IMAGE_HEIGHT / bitmap.height), (MAX_IMAGE_WIDTH / bitmap.width))
        val params = FlexboxLayout.LayoutParams((bitmap.height * scale).roundToInt(), (bitmap.width * scale).roundToInt())
        params.isWrapBefore = true
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageBitmap(bitmap)
        return imageView
    }

}