package com.avisio.dashboard.common.ui.card_image

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.avisio.dashboard.R
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.card_image_view.view.*
import kotlin.math.min
import kotlin.math.roundToInt

class CardImage(context: Context, attributeSet: AttributeSet? = null) : ConstraintLayout(context, attributeSet) {

    companion object {
        private const val MAX_HEIGHT = 150.0
        private const val MAX_WIDTH = 200.0
    }

    init {
        inflate(context, R.layout.card_image_view, this)
    }

    fun setImage(bitmap: Bitmap) {
        val scale = min((MAX_HEIGHT / bitmap.height), (MAX_WIDTH / bitmap.width))
        val params = FlexboxLayout.LayoutParams((bitmap.height * scale).roundToInt(), (bitmap.width * scale).roundToInt())
        params.isWrapBefore = true
        card_image_item.layoutParams = params
        card_image_item.scaleType = ImageView.ScaleType.CENTER_CROP
        card_image_item.setImageBitmap(bitmap)
    }

    fun resetImage() {
        card_image_item.setImageDrawable(null)
    }

}