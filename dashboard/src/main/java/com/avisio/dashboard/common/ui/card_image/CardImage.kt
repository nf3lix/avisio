package com.avisio.dashboard.common.ui.card_image

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.RoundedCornersBitmap
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.card_image_view.view.*
import kotlin.math.min
import kotlin.math.roundToInt

class CardImage(context: Context, attributeSet: AttributeSet? = null) : ConstraintLayout(context, attributeSet) {

    companion object {
        const val IMAGE_CORNER_RADIUS = 40
    }

    private var maxHeight = 150.0
    private var maxWidth = 200.0

    private var deleteImageClickListener: DeleteImageClickListener? = null
    private var currentBitmap: Bitmap? = null
    var showDeleteButton = false

    init {
        inflate(context, R.layout.card_image_view, this)
        card_image_item.setOnClickListener {
            showDeleteImageDialog()
        }
    }

    fun setImage(bitmap: Bitmap) {
        val roundedBitmap = RoundedCornersBitmap.withRoundedCorners(bitmap, IMAGE_CORNER_RADIUS)
        val scale = min((maxHeight / bitmap.height), (maxWidth / bitmap.width))
        val params = FlexboxLayout.LayoutParams((bitmap.height * scale).roundToInt(), (bitmap.width * scale).roundToInt())
        params.isWrapBefore = true
        card_image_item.layoutParams = params
        card_image_item.scaleType = ImageView.ScaleType.CENTER_CROP
        card_image_item.setImageBitmap(roundedBitmap)
        currentBitmap = bitmap
    }

    fun resetImage() {
        card_image_item.setImageDrawable(null)
        currentBitmap = null
    }

    fun setDeleteImageClickListener(listener: DeleteImageClickListener) {
        deleteImageClickListener = listener
    }

    fun setMaxSize(maxHeight: Double, maxWidth: Double) {
        this.maxHeight = maxHeight
        this.maxWidth = maxWidth
        if(currentBitmap != null) {
            setImage(currentBitmap!!)
        }
    }

    private fun showDeleteImageDialog() {
        DeleteImageDialog(this, currentBitmap, deleteImageClickListener, rootView as? ViewGroup).showDialog()
    }

    fun setDeleteButtonVisible(visible: Boolean) {
        showDeleteButton = visible
    }

    interface DeleteImageClickListener {
        fun onClick()
    }

}