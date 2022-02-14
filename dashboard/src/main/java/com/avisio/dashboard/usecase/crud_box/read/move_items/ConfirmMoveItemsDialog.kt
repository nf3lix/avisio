package com.avisio.dashboard.usecase.crud_box.read.move_items

import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.usecase.crud_box.read.BoxListFragment
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class ConfirmMoveItemsDialog(boxListFragment: BoxListFragment, selectedItems: List<DashboardItem>, destination: DashboardItem) {

    companion object {

        fun showDialog(boxListFragment: BoxListFragment, selectedItems: List<DashboardItem>, destination: DashboardItem) {
            val dialog = ConfirmMoveItemsDialog(boxListFragment, selectedItems, destination)
            dialog.showDialog()
        }

    }

    private val confirmDialog: ConfirmDialog = ConfirmDialog(
        boxListFragment.requireContext(),
        boxListFragment.getString(R.string.move_items_title),
        boxListFragment.getString(R.string.move_items_confirm_dialog_message, selectedItems.size.toString(), destination.name)
    )

    init {
        confirmDialog.setOnConfirmListener {

        }
    }

    private fun showDialog() {
        confirmDialog.showDialog()
    }

}