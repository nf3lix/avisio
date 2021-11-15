package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.widget.EditText

data class EditTextSelection(val editText: EditText) {

    private val start: Int = editText.selectionStart.coerceAtMost(editText.selectionEnd)
    private val end: Int = editText.selectionStart.coerceAtLeast(editText.selectionEnd)
    val selectedText = editText.text.toString().substring(start, end)
    val preSelectedText: String = editText.text.toString().substring(0, start)
    val postSelectedText = editText.text.toString().substring(end, editText.text.toString().length)

    fun isEmpty(): Boolean {
        return selectedText.isEmpty()
    }

}