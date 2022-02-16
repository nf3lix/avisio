package com.avisio.dashboard.usecase.crud_box.common

import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.usecase.crud_box.create_box.EditBoxFragment

class BoxNameExistsWarningDialog(fragment: EditBoxFragment) {

    companion object {
        fun showDialog(fragment: EditBoxFragment) {
            BoxNameExistsWarningDialog(fragment).show()
        }
    }

    private val confirmDialog = ConfirmDialog(
        fragment.requireContext(),
        fragment.requireContext().getString(R.string.create_box_duplicate_name_dialog_title),
        fragment.requireContext().getString(R.string.create_box_duplicate_name_dialog_message)
    )

    init {
        confirmDialog.setOnConfirmListener {
            fragment.boxFragmentStrategy.saveBox()
        }
    }

    private fun show() {
        confirmDialog.showDialog()
    }

}