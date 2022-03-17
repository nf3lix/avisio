package com.avisio.dashboard.common.ui.card_image

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.avisio.dashboard.R

class DeleteImageDialog(
    context: Context,
    private val deleteImageClickListener: CardImage.DeleteImageClickListener?,
    view: ViewGroup?
) {

    private val dialogBuilder = AlertDialog.Builder(context).create()
    private val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_delete_card_image, view, false)

    fun showDialog() {
        dialogBuilder
            .setView(viewInflated)
        dialogBuilder.show()
        setDeleteImageClickListener()
    }

    private fun setDeleteImageClickListener() {
        viewInflated.findViewById<ImageView>(R.id.dialog_delete_image_btn).setOnClickListener {
            deleteImageClickListener?.onClick()
            dialogBuilder.dismiss()
        }
    }

}