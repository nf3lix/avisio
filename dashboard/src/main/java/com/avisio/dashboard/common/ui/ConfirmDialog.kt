package com.avisio.dashboard.common.ui

import android.app.AlertDialog
import android.content.Context
import com.avisio.dashboard.R

class ConfirmDialog<T: Any>(
    private val context: Context,
    private val listener: ConfirmDialogListener,
    private val dialogTitle: String,
    private val dialogMessage: String
) {

    fun showDialog(data: T) {
        AlertDialog.Builder(context)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setPositiveButton(context.getText(R.string.confirm_dialog_confirm_default)) { _, _ ->
                listener.onConfirm(data)
            }
            .setNegativeButton(context.getText(R.string.confirm_dialog_cancel_default)) { _, _ ->
                listener.onCancel(data)
            }
            .show()
    }

    interface ConfirmDialogListener {
        fun onConfirm(data: Any)
        fun onCancel(data: Any)
    }

}