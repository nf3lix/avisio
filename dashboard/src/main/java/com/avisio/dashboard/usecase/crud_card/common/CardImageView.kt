package com.avisio.dashboard.usecase.crud_card.common

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.avisio.dashboard.R

class CardImageView(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs) {

    init {
        inflate(context, R.layout.card_image_view, null)
        // layoutParams.width = 50
        // layoutParams.height = 50
    }

    fun setImage(bitmap: Bitmap) {
        setImageBitmap(bitmap)
    }

}