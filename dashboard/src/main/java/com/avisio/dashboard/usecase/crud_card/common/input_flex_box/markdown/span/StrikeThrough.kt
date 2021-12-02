package com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.span

import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class StrikeThrough : MetricAffectingSpan() {

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isStrikeThruText = true
    }

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.isStrikeThruText = true
    }

}