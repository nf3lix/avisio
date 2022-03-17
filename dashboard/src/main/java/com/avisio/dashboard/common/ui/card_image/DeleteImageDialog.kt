package com.avisio.dashboard.common.ui.card_image

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.RoundedCornersBitmap
import kotlin.math.min
import kotlin.math.roundToInt

class DeleteImageDialog(
    context: Context,
    private val bitmap: Bitmap?,
    private val deleteImageClickListener: CardImage.DeleteImageClickListener?,
    view: ViewGroup?
) {

    companion object {
        private const val MAX_IMAGE_HEIGHT = 600.0
        private const val MAX_IMAGE_WIDTH = 800.0
    }

    private val dialogBuilder = AlertDialog.Builder(context).create()
    private val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_delete_card_image, view, false)

    fun showDialog() {
        dialogBuilder
            .setView(viewInflated)
        dialogBuilder.show()
        setImage()
        setDeleteImageClickListener()
    }

    private fun setDeleteImageClickListener() {
        viewInflated.findViewById<ImageView>(R.id.dialog_delete_image_btn).setOnClickListener {
            deleteImageClickListener?.onClick()
            dialogBuilder.dismiss()
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

}