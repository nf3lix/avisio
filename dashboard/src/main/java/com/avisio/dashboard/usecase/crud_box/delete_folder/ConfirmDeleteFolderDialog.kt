package com.avisio.dashboard.usecase.crud_box.delete_folder

import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.usecase.crud_box.read.BoxListFragment

class ConfirmDeleteFolderDialog(fragment: BoxListFragment) {

    companion object {

        fun showDialog(fragment: BoxListFragment) {
            val dialog = ConfirmDeleteFolderDialog(fragment)
            dialog.showDialog()
        }

    }

    private val confirmDialog: ConfirmDialog = ConfirmDialog(
        fragment.requireContext(),
        fragment.requireContext().getString(R.string.action_delete_folder),
        fragment.requireContext().getString(R.string.delete_folder_confirm_message)
    )

    init {
        confirmDialog.setOnConfirmListener {
            fragment.deleteCurrentFolder()
        }
    }

    private fun showDialog() {
        confirmDialog.showDialog()
    }

}