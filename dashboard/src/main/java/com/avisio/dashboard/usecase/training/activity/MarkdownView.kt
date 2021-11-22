package com.avisio.dashboard.usecase.training.activity

import android.widget.TextView
import io.noties.markwon.Markwon

object MarkdownView {

    fun enableMarkdown(markwon: Markwon, textView: TextView) {
        markwon.setMarkdown(textView, textView.text.toString())
    }

}