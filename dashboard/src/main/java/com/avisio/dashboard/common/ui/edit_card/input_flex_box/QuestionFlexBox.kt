package com.avisio.dashboard.common.ui.edit_card.input_flex_box

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.google.android.material.chip.Chip

class QuestionFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet) {

    companion object {
        private const val TEXT_SIZE = 14F
    }

    private lateinit var toolbar: CardQuestionInputToolbar

    init {
        initToolbar()
        addInitialEditText("")
    }

    private fun addClozeChip() {
        val (editTextSelection, selectionEditTextIndex) = getSelectedText()
        if(editTextSelection.isEmpty()) return
        replaceTextEditByChip(selectionEditTextIndex, editTextSelection)
        mergeRemainingEditTexts()
        resetInformation()
        cardChangeListener.onCardTypeSet(CardType.CLOZE_TEXT)
    }

    private fun getSelectedText(): Pair<EditTextSelection, Int> {
        var editTextSelection = EditTextSelection(EditText(context))
        var selectionEditTextIndex = -1
        for((index, view) in flexbox.allViews.toList().withIndex()) {
            if(view !is EditText) continue
            editTextSelection = EditTextSelection(view)
            if(!editTextSelection.isEmpty()) {
                selectionEditTextIndex = index
                view.setText("")
                break
            }
        }
        return Pair(editTextSelection, selectionEditTextIndex)
    }

    private fun replaceTextEditByChip(editTextPosition: Int, selection: EditTextSelection) {
        val chip = getClozeChip(selection.selectedText)
        flexbox.removeView(selection.editText)
        flexbox.addView(chip as View, editTextPosition - 1)
        val preEditText = getEditText(selection.preSelectedText)
        val postEditText = getEditText(selection.postSelectedText)
        flexbox.addView(preEditText as View, editTextPosition - 1)
        flexbox.addView(postEditText as View, editTextPosition + 1)
        chip.setOnCloseIconClickListener { flexbox.removeView(chip as View) }
    }

    private fun mergeRemainingEditTexts() {
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
                    val editText = getEditText(token.content.trim())
                    flexbox.addView(editText, index)
                }
                CardQuestionTokenType.QUESTION -> {
                    val chip = getClozeChip(token.content.trim())
                    flexbox.addView(chip, index)
                }
            }
        }
        mergeRemainingEditTexts()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getClozeChip(question: String): Chip {
        val chip = Chip(context)
        chip.text = question
        chip.setChipBackgroundColorResource(R.color.primaryDarkColor)
        chip.textSize = TEXT_SIZE
        chip.tag = false
        chip.setOnTouchListener{ view, motionEvent ->
            closeChip(chip, view, motionEvent)
            true
        }

        chip.isCloseIconVisible = true
        chip.isCheckable = false
        return chip
    }

    private fun closeChip(chip: Chip, view: View, motionEvent: MotionEvent) {
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
                editTextReplacement.setText(chip.text)
                flexbox.removeView(chip)
                flexbox.addView(editTextReplacement as View, chipIndex - 1)
            }
            mergeRemainingEditTexts()
            setEditTextKeyListeners()
            checkCardType()
        }
    }

    fun replaceClozeTextByStandardQuestion() {
        val previousQuestion = getCardQuestion()
        val newQuestionTokenList = arrayListOf<CardQuestionToken>()
        for(token in previousQuestion.tokenList) {
            newQuestionTokenList.add(CardQuestionToken(token.content, CardQuestionTokenType.TEXT))
        }
        flexbox.removeAllViews()
        setCardQuestion(CardQuestion(newQuestionTokenList))
    }

    private fun checkCardType() {
        val views = flexbox.allViews.toList()
        for(view in views) {
            if(view is Chip) {
                cardChangeListener.onCardTypeSet(CardType.CLOZE_TEXT)
                return
            }
        }
        cardChangeListener.onCardTypeSet(CardType.STANDARD)
    }

    private fun addInitialEditText(input: String) {
        val editText = getEditText(input)
        editText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        editText.setOnKeyListener { _, _, _ ->
            setEditTextKeyListeners()
            true
        }
        flexbox.addView(editText as View, 0)
    }

    private fun getEditText(input: String): EditText {
        val editText = EditText(context)
        editText.setText(input)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.textSize = TEXT_SIZE
        }
        return editText
    }

    private fun setEditTextKeyListeners() {
        val views = flexbox.allViews.toList()
        for(view in views) {
            if(view is EditText) {
                view.setOnKeyListener { _, _, _ ->
                    resetInformation()
                    false
                }
            }
        }
    }

    override fun initToolbar() {
        toolbar = CardQuestionInputToolbar(context)
        toolbarContainer.addView(toolbar as View)
        toolbar.clozeTextButton.setOnClickListener {
            addClozeChip()
        }
    }

}