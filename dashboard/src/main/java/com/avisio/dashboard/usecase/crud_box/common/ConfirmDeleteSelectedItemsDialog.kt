package com.avisio.dashboard.usecase.crud_box.common

import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.usecase.crud_box.read.BoxListFragment

class ConfirmDeleteSelectedItemsDialog(fragment: BoxListFragment) {

    companion object {

        fun showDialog(fragment: BoxListFragment) {
            val dialog = ConfirmDeleteSelectedItemsDialog(fragment)
            dialog.showDialog()
        }

    }

    private val confirmDialog: ConfirmDialog = ConfirmDialog(
        fragment.requireContext(),
        fragment.requireContext().getString(R.string.delete_all_selected_items_confirm_dialog_title),
        fragment.requireContext().getString(R.string.delete_all_selected_items_confirm_dialog_message),
    )

    init {
        confirmDialog.setOnConfirmListener {
            fragment.deleteAllSelectedItems()
        }
    }

    private fun showDialog() {
        confirmDialog.showDialog()
    }

}