package com.avisio.dashboard.common.ui.edit_card

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.allViews
import androidx.core.view.isVisible
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip

class QuestionFlexBox(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    companion object {
        private const val TEXT_SIZE = 14F
    }

    private var button: Button
    private var flexbox: FlexboxLayout

    init {
        inflate(context, R.layout.question_flex_box, this)
        button = findViewById(R.id.test_button)
        flexbox = findViewById(R.id.test_flexbox)
        button.setOnClickListener {
            addChip()
        }
        val editText = getEditText("")
        editText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        editText.setOnKeyListener { _, _, _ ->
            setEditTextKeyListeners()
            true
        }
        flexbox.addView(editText as View, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addChip() {
        var preSelectedText = ""
        var postSelectedText = ""
        var selectedText = ""
        var selectionEditTextIndex = -1
        for((index, view) in flexbox.allViews.toList().withIndex()) {
            if(view !is EditText) continue
            val start = view.selectionStart
            val end = view.selectionEnd
            if(end - start != 0) {
                selectedText = view.text.toString().substring(start, end)
                preSelectedText = view.text.toString().substring(0, start)
                postSelectedText = view.text.toString().substring(end, view.text.toString().length)
                selectionEditTextIndex = index
                view.setText("")
            }
        }

        if(selectedText == "") {
            return
        }

        val chip = getClozeChip(selectedText)
        flexbox.removeView(flexbox.allViews.toList()[selectionEditTextIndex])
        flexbox.addView(chip as View, selectionEditTextIndex - 1)
        val preEditText = getEditText(preSelectedText)
        val postEditText = getEditText(postSelectedText)
        flexbox.addView(preEditText as View, selectionEditTextIndex - 1)
        flexbox.addView(postEditText as View, selectionEditTextIndex + 1)
        chip.setOnCloseIconClickListener { flexbox.removeView(chip as View) }
        mergeEditTexts()
        resetError()
    }

    private fun mergeEditTexts() {
        val viewList = flexbox.allViews.toList()
        val editTextMap = HashMap<Int, String>()
        for((index, view) in viewList.withIndex()) {
            if(view is EditText) {
                editTextMap[index] = view.text.toString()
            }
        }
        var previousEditTextIndex = -1
        for((editTextIndex, text) in editTextMap) {
            if(editTextIndex == 1) {
                previousEditTextIndex = editTextIndex
                continue
            }
            if(editTextIndex == previousEditTextIndex + 1) {
                val mergedText = editTextMap[previousEditTextIndex]!!.trim() + " " + text.trim()
                (viewList[editTextIndex] as EditText).setText(mergedText)
                editTextMap[editTextIndex] = mergedText
                flexbox.removeView(viewList[previousEditTextIndex] as EditText)
            }
            previousEditTextIndex = editTextIndex
        }
    }

    fun getCardQuestion(): CardQuestion {
        val tokenList = arrayListOf<CardQuestionToken>()
        for(view in flexbox.allViews.toList()) {
            when(view) {
                is EditText -> {
                    tokenList.add(CardQuestionToken(view.text.toString(), CardQuestionTokenType.TEXT))
                }
                is Chip -> {
                    tokenList.add(CardQuestionToken(view.text.toString(), CardQuestionTokenType.QUESTION))
                }
            }
        }
        return CardQuestion(tokenList)
    }

    fun setCardQuestion(cardQuestion: CardQuestion) {
        flexbox.removeAllViews()
        for((index, token) in cardQuestion.tokenList.withIndex()) {
            when(token.tokenType) {
                CardQuestionTokenType.TEXT -> {
                    val editText = getEditText(token.content)
                    flexbox.addView(editText, index)
                }
                CardQuestionTokenType.QUESTION -> {
                    val chip = getClozeChip(token.content)
                    flexbox.addView(chip, index)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getClozeChip(question: String): Chip {
        val chip = Chip(context)
        chip.text = question
        chip.setChipBackgroundColorResource(R.color.primaryDarkColor)
        chip.textSize = TEXT_SIZE
        chip.tag = false
        chip.setOnTouchListener{ view, motionEvent ->
            if(view is Chip && view.tag == false) {
                if(motionEvent.x >= view.totalPaddingRight) {
                    chip.tag = true
                    var chipIndex = 0
                    for((index, flexboxView) in flexbox.allViews.toList().withIndex()) {
                        if(flexboxView == chip) {
                            chipIndex = index
                        }
                    }
                    val editTextReplacement = EditText(context)
                    editTextReplacement.setText(question)
                    flexbox.removeView(chip)
                    flexbox.addView(editTextReplacement as View, chipIndex - 1)
                }
                mergeEditTexts()
                setEditTextKeyListeners()
            }
            true
        }

        chip.isCloseIconVisible = true
        chip.isCheckable = false
        return chip
    }

    private fun getEditText(input: String): EditText {
        val editText = EditText(context)
        editText.setText(input)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.textSize = TEXT_SIZE
        }
        return editText
    }

    fun setError(errorMessage: String) {
        val errorMessageView = findViewById<TextView>(R.id.question_box_error_message)
        errorMessageView.visibility = View.VISIBLE
        errorMessageView.text = errorMessage
        findViewById<TextView>(R.id.question_title_text_view).setTextColor(resources.getColor(R.color.warning))
    }

    fun resetError() {
        findViewById<TextView>(R.id.question_title_text_view).setTextColor(resources.getColor(R.color.primaryColor))
        val errorMessageView = findViewById<TextView>(R.id.question_box_error_message)
        errorMessageView.visibility = View.GONE
        errorMessageView.text = ""
    }

    private fun setEditTextKeyListeners() {
        val views = flexbox.allViews.toList()
        for(view in views) {
            if(view is EditText) {
                view.setOnKeyListener { _, _, _ ->
                    Log.d("test123", "test")
                    resetError()
                    false
                }
            }
        }
    }

}