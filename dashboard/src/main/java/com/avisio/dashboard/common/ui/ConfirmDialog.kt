package com.avisio.dashboard.common.ui

import android.app.AlertDialog
import android.content.Context

class ConfirmDialog<T: Any>(
    private val context: Context,
    private val listener: ConfirmDialogListener,
    private val dialogTitle: String,
    private val dialogMessage: String
) {

    fun showDialog(data: T) {
        // TODO: move strings to config
        AlertDialog.Builder(context)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setPositiveButton("Ok") { _, _ ->
                listener.onConfirm(data)
            }
            .setNegativeButton("Cancel") { _, _ ->
                listener.onCancel(data)
            }
            .show()
    }

    interface ConfirmDialogListener {
        fun onConfirm(data: Any)
        fun onCancel(data: Any)
    }

}