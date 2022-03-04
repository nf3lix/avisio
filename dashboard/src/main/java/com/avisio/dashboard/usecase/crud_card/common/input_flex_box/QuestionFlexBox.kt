package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint.TargetInput.QUESTION_INPUT
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import kotlin.math.min
import kotlin.math.roundToInt

class QuestionFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet, QUESTION_INPUT) {

    companion object {
        const val TEXT_SIZE = 14F
        private const val MAX_IMAGE_HEIGHT = 150.0
        private const val MAX_IMAGE_WIDTH = 200.0
    }

    private lateinit var toolbar: CardQuestionInputToolbar
    private var markdownDisabled: Boolean = false
    private var selectImageObserver: SelectImageObserver? = null

    init {
        initToolbar()
        addInitialEditText("")
    }

    private fun addClozeChip() {
        val (editTextSelection, selectionEditTextIndex) = getSelectedText()
        if(editTextSelection.isEmpty()) {
            setAddClozeButtonExplanation()
            return
        }
        replaceTextEditByChip(selectionEditTextIndex, editTextSelection)
        mergeRemainingEditTexts()
        resetInformation()
        cardChangeListener.onCardTypeSet(CardType.CLOZE_TEXT)
        cardChangeListener.onFlexboxInputChanged(this)
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

    private fun setAddClozeButtonExplanation() {
        setInformation(context.getString(R.string.create_card_add_cloze_button_explanation))
    }

    private fun replaceTextEditByChip(editTextPosition: Int, selection: EditTextSelection) {
        val chip = getClozeChip(selection.selectedText)
        flexbox.removeView(selection.editText)
        flexbox.addView(chip as View, editTextPosition - 1)
        val preEditText = getEditText(selection.preSelectedText.trim())
        val postEditText = getEditText(selection.postSelectedText.trim())
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
        stretchSingleEditText()
        setEditTextKeyListeners()
        initMarkdown()
    }

    fun getCardQuestion(trimmed: Boolean = false): CardQuestion {
        val tokenList = arrayListOf<QuestionToken>()
        for(view in flexbox.allViews.toList()) {
            when(view) {
                is EditText -> {
                    if(view.text.isNotEmpty() || !trimmed) {
                        tokenList.add(QuestionToken(view.text.toString(), QuestionTokenType.TEXT))
                    }
                }
                is Chip -> {
                    tokenList.add(QuestionToken(view.text.toString(), QuestionTokenType.QUESTION))
                }
                is ImageView -> {
                    val tag = view.tag as String
                    tokenList.add(QuestionToken(tag, QuestionTokenType.IMAGE))
                }
            }
        }
        return CardQuestion(tokenList)
    }

    fun setCardQuestion(cardQuestion: CardQuestion) {
        flexbox.removeAllViews()
        var count = 0
        for(token in cardQuestion.tokenList) {
            when(token.tokenType) {
                QuestionTokenType.TEXT -> {
                    val editText = getEditText(token.content.trim())
                    flexbox.addView(editText, count)
                    count++
                }
                QuestionTokenType.QUESTION -> {
                    val chip = getClozeChip(token.content.trim())
                    flexbox.addView(chip, count)
                    count++
                }
                QuestionTokenType.IMAGE -> {
                    val byteArray = context.openFileInput(token.content).readBytes()
                    val decodedImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    val imageView = getImageView(decodedImage)
                    imageView.tag = token.content
                    val et1 = getEditText("")
                    val layoutParams1 = FlexboxLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    et1.layoutParams = layoutParams1
                    flexbox.addView(et1, count)
                    count++
                    flexbox.addView(imageView, count)
                    count++
                    val et2 = getEditText("")
                    val layoutParams2 = FlexboxLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    layoutParams2.isWrapBefore = true
                    et2.layoutParams = layoutParams2
                    flexbox.addView(et2, count)
                }
            }
        }
        mergeRemainingEditTexts()
    }

    private fun stretchSingleEditText() {
        if(flexbox.childCount == 1 && flexbox.getChildAt(0) is EditText) {
            val previousText = (flexbox.getChildAt(0) as EditText).text.toString()
            val stretchedEditText = getEditText(previousText.trim())
            stretchedEditText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, WRAP_CONTENT)
            flexbox.removeAllViews()
            flexbox.addView(stretchedEditText, 0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getClozeChip(question: String): Chip {
        val chip = Chip(context)
        chip.text = question
        chip.setChipBackgroundColorResource(R.color.primaryDarkColor)
        chip.textSize = TEXT_SIZE
        chip.tag = false
        chip.setOnTouchListener { view, motionEvent ->
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
                cardChangeListener.onFlexboxInputChanged(this)
            }
            mergeRemainingEditTexts()
            setEditTextKeyListeners()
            enableMarkdown()
            checkCardType()
        }
    }

    fun replaceClozeTextByStandardQuestion() {
        val previousQuestion = getCardQuestion()
        val newQuestionTokenList = arrayListOf<QuestionToken>()
        for(token in previousQuestion.tokenList) {
            when(token.tokenType) {
                QuestionTokenType.IMAGE -> {
                    newQuestionTokenList.add(QuestionToken(token.content, QuestionTokenType.IMAGE))
                }
                else -> {
                    newQuestionTokenList.add(QuestionToken(token.content, QuestionTokenType.TEXT))
                }
            }
        }
        flexbox.removeAllViews()
        setCardQuestion(CardQuestion(newQuestionTokenList))
        cardChangeListener.onFlexboxInputChanged(this)
    }

    private fun checkCardType() {
        val views = flexbox.allViews.toList()
        markdownDisabled = false
        for(view in views) {
            if(view is Chip) {
                cardChangeListener.onCardTypeSet(CardType.CLOZE_TEXT)
                return
            }
        }
        if(!markdownDisabled && markdown.isEnabled()) {
            cardChangeListener.onCardTypeSet(CardType.STANDARD)
            return
        }
        cardChangeListener.onCardTypeSet(CardType.STRICT)
    }

    private fun addInitialEditText(input: String) {
        val editText = getEditText(input)
        editText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, WRAP_CONTENT)
        editText.setOnKeyListener { _, _, _ ->
            setEditTextKeyListeners()
            true
        }
        flexbox.addView(editText as View, 0)
        initMarkdown()
    }

    private fun getEditText(input: String): EditText {
        val editText = EditText(context)
        editText.setText(input)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.textSize = TEXT_SIZE
        }
        return editText
    }

    private fun getImageView(bitmap: Bitmap): ImageView {
        val imageView = ImageView(context)
        val scale = min((MAX_IMAGE_HEIGHT / bitmap.height), (MAX_IMAGE_WIDTH / bitmap.width))
        // val params = ViewGroup.LayoutParams((bitmap.height * scale).roundToInt(), (bitmap.width * scale).roundToInt())
        val params = FlexboxLayout.LayoutParams((bitmap.height * scale).roundToInt(), (bitmap.width * scale).roundToInt())
        params.isWrapBefore = true
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageBitmap(bitmap)
        return imageView
    }

    private fun setEditTextKeyListeners() {
        val views = flexbox.allViews.toList()
        for(view in views) {
            if(view is EditText) {
                view.addTextChangedListener(CardInputKeyTextWatcher(cardChangeListener, this))
            }
        }
    }

    override fun initToolbar() {
        toolbar = CardQuestionInputToolbar(context)
        toolbarContainer.addView(toolbar as View)
        toolbar.clozeTextButton.setOnClickListener {
            addClozeChip()
        }
        toolbar.selectImageButton.setOnClickListener {
            this.selectImageObserver?.onStartSelect()
        }
    }

    fun setSelectImageObserver(selectImageObserver: SelectImageObserver) {
        this.selectImageObserver = selectImageObserver
    }

    override fun resetEditText() { }

}