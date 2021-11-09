package com.avisio.dashboard.common.ui.edit_card

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.allViews
import com.avisio.dashboard.R
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip

class QuestionFlexBox(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private var button: Button
    private var editText: EditText
    private var flexbox: FlexboxLayout

    init {
        inflate(context, R.layout.question_flex_box, this)
        button = findViewById(R.id.test_button)
        editText = findViewById(R.id.test_edit_text)
        flexbox = findViewById(R.id.test_flexbox)
        button.setOnClickListener {
            addChip()
        }
    }

    private fun addChip() {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val selection = editText.text.toString().substring(start, end)
        val chip = Chip(context)
        chip.text = selection
        chip.isCloseIconEnabled = true
        chip.isClickable = true
        chip.isCheckable = false
        flexbox.addView(chip as View, flexbox.childCount - 1)
        editText.setText("")
        flexbox.addView(EditText(context) as View, 0)
        chip.setOnCloseIconClickListener { flexbox.removeView(chip as View) }
        mergeEditTexts()
    }

    private fun mergeEditTexts() {
        val viewList = flexbox.allViews.toList()
        val editTextIndexList = HashMap<Int, String>()
        for((index, view) in viewList.withIndex()) {
            if(view is EditText) {
                editTextIndexList[index] = view.text.toString()
            }
        }
        Log.d("test12345", editTextIndexList.toString())

    }

}