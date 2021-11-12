package com.avisio.dashboard.common.ui.edit_card.input_flex_box

import android.widget.EditText

class EditTextSelection(val editText: EditText) {

    private val start: Int = editText.selectionStart.coerceAtMost(editText.selectionEnd)
    private val end: Int = editText.selectionStart.coerceAtLeast(editText.selectionEnd)
    val selectedText = editText.text.toString().substring(start, end)
    val preSelectedText: String = editText.text.toString().substring(0, start)
    val postSelectedText = editText.text.toString().substring(end, editText.text.toString().length)

    fun isEmpty(): Boolean {
        return selectedText.isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EditTextSelection

        if (editText != other.editText) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (selectedText != other.selectedText) return false
        if (preSelectedText != other.preSelectedText) return false
        if (postSelectedText != other.postSelectedText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = editText.hashCode()
        result = 31 * result + start
        result = 31 * result + end
        result = 31 * result + selectedText.hashCode()
        result = 31 * result + preSelectedText.hashCode()
        result = 31 * result + postSelectedText.hashCode()
        return result
    }


}