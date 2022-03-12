package com.avisio.dashboard.common.ui.card_image

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.avisio.dashboard.R
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.card_image_view.view.*
import kotlin.math.min
import kotlin.math.roundToInt

class CardImage(context: Context, attributeSet: AttributeSet? = null) : ConstraintLayout(context, attributeSet) {

    var maxHeight = 150.0
    var maxWidth = 200.0

    private var deleteImageClickListener: DeleteImageClickListener? = null
    private var currentBitmap: Bitmap? = null

    init {
        inflate(context, R.layout.card_image_view, this)
        delete_image_btn.setOnClickListener {
            deleteImageClickListener?.onClick()
        }
    }

    fun setImage(bitmap: Bitmap) {
        val scale = min((maxHeight / bitmap.height), (maxWidth / bitmap.width))
        val params = FlexboxLayout.LayoutParams((bitmap.height * scale).roundToInt(), (bitmap.width * scale).roundToInt())
        params.isWrapBefore = true
        card_image_item.layoutParams = params
        card_image_item.scaleType = ImageView.ScaleType.CENTER_CROP
        card_image_item.setImageBitmap(bitmap)
        currentBitmap = bitmap
    }

    fun resetImage() {
        card_image_item.setImageDrawable(null)
        currentBitmap = null
    }

    fun setDeleteImageClickListener(listener: DeleteImageClickListener) {
        deleteImageClickListener = listener
    }

    interface DeleteImageClickListener {
        fun onClick()
    }

    fun setMaxSize(maxHeight: Double, maxWidth: Double) {
        this.maxHeight = maxHeight
        this.maxWidth = maxWidth
        if(currentBitmap != null) {
            setImage(currentBitmap!!)
        }
    }

    fun setDeleteImageButtonVisible(visible: Boolean) {
        if(visible) {
            delete_image_btn.visibility = View.VISIBLE
        } else {
            delete_image_btn.visibility = View.GONE
        }
    }

}