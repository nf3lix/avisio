package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.handlers

import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.editor.EditHandler

abstract class AvisioMarkdownEditHandler<T: Any> : EditHandler<T> {

    internal var markdownTheme: MarkwonTheme? = null

    override fun init(markwon: Markwon) {
        markdownTheme = markwon.configuration().theme()
    }

}