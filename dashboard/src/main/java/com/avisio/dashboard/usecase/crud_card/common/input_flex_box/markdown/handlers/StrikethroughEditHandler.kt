package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.handlers

import android.text.Editable
import android.text.Spanned
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.span.StrikeThrough
import io.noties.markwon.editor.AbstractEditHandler
import io.noties.markwon.editor.MarkwonEditorUtils
import io.noties.markwon.editor.PersistedSpans

class StrikethroughEditHandler : AbstractEditHandler<StrikeThrough>() {

    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(StrikeThrough::class.java) { StrikeThrough() }
    }

    override fun handleMarkdownSpan(persistedSpans: PersistedSpans, editable: Editable, input: String,
                                    span: StrikeThrough, spanStart: Int, spanTextLength: Int
    ) {
        val match = MarkwonEditorUtils.findDelimited(input, spanStart, "~~", "~~")
        if(match != null) {
            editable.setSpan(
                persistedSpans.get(StrikeThrough::class.java),
                match.start(),
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun markdownSpanType(): Class<StrikeThrough> {
        return StrikeThrough::class.java
    }

}