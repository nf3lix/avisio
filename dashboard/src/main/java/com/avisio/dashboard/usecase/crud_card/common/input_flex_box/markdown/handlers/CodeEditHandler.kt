package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.handlers

import android.text.Editable
import android.text.Spanned
import io.noties.markwon.core.spans.CodeSpan
import io.noties.markwon.editor.MarkwonEditorUtils
import io.noties.markwon.editor.PersistedSpans

class CodeEditHandler : AvisioMarkdownEditHandler<CodeSpan>() {

    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(CodeSpan::class.java) { CodeSpan(markdownTheme!!) }
    }

    override fun handleMarkdownSpan(persistedSpans: PersistedSpans, editable: Editable, input: String,
                                    span: CodeSpan, spanStart: Int, spanTextLength: Int
    ) {
        val match = MarkwonEditorUtils.findDelimited(input, spanStart, "`")
        if (match != null) {
            editable.setSpan(
                persistedSpans.get(CodeSpan::class.java),
                match.start(),
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun markdownSpanType(): Class<CodeSpan> {
        return CodeSpan::class.java
    }
}