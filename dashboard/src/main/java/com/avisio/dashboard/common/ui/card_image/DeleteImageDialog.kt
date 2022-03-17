package com.avisio.dashboard.common.ui.card_image

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avisio.dashboard.R

class DeleteImageDialog(
    context: Context,
    view: ViewGroup?
) {

    private val dialogBuilder = AlertDialog.Builder(context)
    private val viewInflated: View = LayoutInflater.from(context).inflate(R.layout.dialog_delete_card_image, view, false)

    fun showDialog() {
        dialogBuilder
            .setView(viewInflated)
        dialogBuilder.show()
    }

}