package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.handlers

import android.text.Editable
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import io.noties.markwon.core.spans.LinkSpan
import io.noties.markwon.editor.PersistedSpans

/*
source: https://github.com/noties/Markwon/blob/master/app-sample/src/main/java/io/noties/markwon/app/samples/editor/shared/LinkEditHandler.java
date: 11-15-21
 */
class LinkEditHandler(private val onClick: OnClick) : AvisioMarkdownEditHandler<LinkSpan>() {

    companion object {
        val DEFAULT_CLICK_LISTENER = object : OnClick {
            override fun onClick(widget: View, link: String) { }
        }
    }

    interface OnClick {
        fun onClick(widget: View, link: String)
    }

    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(EditLinkSpan::class.java) { EditLinkSpan(onClick) }
    }

    override fun handleMarkdownSpan(persistedSpans: PersistedSpans, editable: Editable, input: String,
                                    span: LinkSpan, spanStart: Int, spanTextLength: Int) {
        val editLinkSpan = persistedSpans[EditLinkSpan::class.java]
        editLinkSpan.link = span.link
        var start = -1
        var i = spanStart
        val length = input.length
        while (i < length) {
            if (Character.isLetter(input[i])) {
                start = i
                break
            }
            i++
        }
        if (start > -1) {
            editable.setSpan(
                editLinkSpan,
                start,
                start + spanTextLength,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun markdownSpanType(): Class<LinkSpan> {
        return LinkSpan::class.java
    }

    internal class EditLinkSpan(private val onClick: OnClick) :
        ClickableSpan() {
        var link: String? = null
        override fun onClick(widget: View) {
            if (link != null) {
                onClick.onClick(widget, link!!)
            }
        }
    }


}