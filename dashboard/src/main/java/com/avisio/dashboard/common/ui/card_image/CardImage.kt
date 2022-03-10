package com.avisio.dashboard.common.ui.card_image

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.avisio.dashboard.R
import kotlin.math.min

class CardImage(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    companion object {
        private const val MAX_HEIGHT = 300.0
        private const val MAX_WIDTH = 400.0
    }

    init {
        inflate(context, R.layout.card_image_view, this)
    }

    fun setImage(bitmap: Bitmap) {
        //val scale = min((MAX_HEIGHT / bitmap.height), (M))
    }

}