package com.avisio.dashboard.common.ui

import android.app.AlertDialog
import android.content.Context
import com.avisio.dashboard.R

class ConfirmDialog(
    private val context: Context,
    private val dialogTitle: String,
    private val dialogMessage: String,
    private val positiveButtonText: Int = R.string.confirm_dialog_confirm_default,
    private val negativeButtonText: Int = R.string.confirm_dialog_cancel_default,

) {

    private val dialogBuilder = AlertDialog.Builder(context)

    init {
        setDefaultClickListeners()
    }

    fun showDialog() {
        dialogBuilder
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .show()
    }

    fun setOnConfirmListener(onClick: () -> Unit) {
        dialogBuilder.setPositiveButton(context.getText(positiveButtonText)) { _, _ ->
            onClick()
        }
    }

    fun setOnCancelListener(onClick: () -> Unit) {
        dialogBuilder.setNegativeButton(context.getText(negativeButtonText)) { _, _ ->
            onClick()
        }
    }

    private fun setDefaultClickListeners() {
        dialogBuilder.setNegativeButton(context.getText(negativeButtonText)) { _, _ -> }
        dialogBuilder.setPositiveButton(context.getText(positiveButtonText)) { _, _ -> }
    }
}