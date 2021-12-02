package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.handlers

import android.text.Editable
import android.text.Spanned
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.core.spans.BlockQuoteSpan
import io.noties.markwon.editor.EditHandler
import io.noties.markwon.editor.PersistedSpans

/*
 source: https://github.com/noties/Markwon/blob/master/app-sample/src/main/java/io/noties/markwon/app/samples/editor/shared/BlockQuoteEditHandler.java
 date: 11-15-21
 */
class BlockQuoteEditHandler : EditHandler<BlockQuoteSpan> {

    private var markdownTheme: MarkwonTheme? = null

    override fun init(markwon: Markwon) {
        markdownTheme = markwon.configuration().theme()
    }

    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(BlockQuoteSpan::class.java) { BlockQuoteSpan(markdownTheme!!) }
    }

    override fun handleMarkdownSpan(persistedSpans: PersistedSpans, editable: Editable, input: String,
        span: BlockQuoteSpan, spanStart: Int, spanTextLength: Int
    ) {
        editable.setSpan(
            persistedSpans.get(BlockQuoteSpan::class.java),
            spanStart,
            spanStart + spanTextLength,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    override fun markdownSpanType(): Class<BlockQuoteSpan> {
        return BlockQuoteSpan::class.java
    }

}