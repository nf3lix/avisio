package com.avisio.dashboard.common.ui.card_image

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.RoundedCornersBitmap
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class DeleteImageDialog(
    private val cardImage: CardImage,
    private val bitmap: Bitmap?,
    private val deleteImageClickListener: CardImage.DeleteImageClickListener?,
    view: ViewGroup?
) {

    companion object {
        private const val MAX_IMAGE_HEIGHT = 600.0
        private const val MAX_IMAGE_WIDTH = 800.0
    }

    private val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(cardImage.context, ScaleListener())
    private var scaleFactor = 1F

    private val dialogBuilder = AlertDialog.Builder(cardImage.context).create()
    private val viewInflated: View = LayoutInflater.from(cardImage.context).inflate(R.layout.dialog_delete_card_image, view, false)

    fun showDialog() {
        dialogBuilder
            .setView(viewInflated)
        dialogBuilder.show()
        setImage()
        test()
        setDeleteImageButton()
        setDeleteImageClickListener()
    }

    private fun setDeleteImageClickListener() {
        if(cardImage.showDeleteButton) {
            viewInflated.findViewById<ImageView>(R.id.dialog_delete_image_btn).setOnClickListener {
                deleteImageClickListener?.onClick()
                dialogBuilder.dismiss()
            }
        }
    }

    private fun setImage() {
        if(bitmap == null) {
            return
        }
        val roundedBitmap = RoundedCornersBitmap.withRoundedCorners(bitmap, 40)
        val scale = min((MAX_IMAGE_HEIGHT / roundedBitmap.height), (MAX_IMAGE_WIDTH / roundedBitmap.width))
        val params = LinearLayout.LayoutParams((roundedBitmap.height * scale).roundToInt(), (roundedBitmap.width * scale).roundToInt())
        params.gravity = Gravity.CENTER_HORIZONTAL
        viewInflated.findViewById<ImageView>(R.id.card_image_item).layoutParams = params
        viewInflated.findViewById<ImageView>(R.id.card_image_item).setImageBitmap(roundedBitmap)
    }

    private fun setDeleteImageButton() {
        if(cardImage.showDeleteButton) {
            viewInflated.findViewById<ImageView>(R.id.dialog_delete_image_btn).visibility = View.VISIBLE
        }
    }

    private fun test() {
        viewInflated.findViewById<ImageView>(R.id.card_image_item).setOnTouchListener { _: View, event: MotionEvent ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            val imageView = viewInflated.findViewById<ImageView>(R.id.card_image_item)
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.6F, min(scaleFactor, 3F))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            Log.d("scaleFactor", scaleFactor.toString())
            return super.onScale(detector)
        }
    }

}