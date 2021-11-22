package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.handlers.*
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.noties.markwon.editor.handler.EmphasisEditHandler
import io.noties.markwon.editor.handler.StrongEmphasisEditHandler
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import java.util.concurrent.Executors

class Markdown(val editText: EditText, private val targetView: TextView) {

    private lateinit var markwon: Markwon
    private lateinit var editor: MarkwonEditor
    private lateinit var markdownEditTextChangedListener: TextWatcher
    private lateinit var targetEditTextChangedListener: TextWatcher

    private var enabled = false

    init {
        initMarkdown()
        initEditor()
        initTextWatchers()
        enable()
    }

    private fun initMarkdown() {
        markwon = Markwon.builder(editText.context)
            .usePlugin(SoftBreakAddsNewLinePlugin.create())
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(HtmlPlugin.create())
            .usePlugin(JLatexMathPlugin.create(targetView.textSize) { it.inlinesEnabled(true) })
            .build()
    }

    private fun initEditor() {
        editor = MarkwonEditor.builder(markwon)
            .useEditHandler(EmphasisEditHandler())
            .useEditHandler(StrongEmphasisEditHandler())
            .useEditHandler(StrikethroughEditHandler())
            .useEditHandler(CodeEditHandler())
            .useEditHandler(BlockQuoteEditHandler())
            .useEditHandler(HeadingEditHandler())
            .useEditHandler(LinkEditHandler(LinkEditHandler.DEFAULT_CLICK_LISTENER))
            .build()
    }

    private fun initTextWatchers() {
        markdownEditTextChangedListener = MarkwonEditorTextWatcher.withPreRender(
            editor, Executors.newCachedThreadPool(), editText)

        targetEditTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                markwon.setMarkdown(targetView, editText.text.toString())
            }

        }
    }

    fun enable() {
        editText.addTextChangedListener(markdownEditTextChangedListener)
        editText.addTextChangedListener(targetEditTextChangedListener)
        markwon.setMarkdown(targetView, editText.text.toString())
        enabled = true
    }

    fun disable() {
        editText.removeTextChangedListener(markdownEditTextChangedListener)
        editText.removeTextChangedListener(targetEditTextChangedListener)
        enabled = false
        val prevInput = editText.text.toString()
        editText.setText("")
        editText.setText(prevInput)
    }

    fun isEnabled(): Boolean {
        return enabled
    }

}