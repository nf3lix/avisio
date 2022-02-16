package com.avisio.dashboard.usecase.crud_box.read.move_items

import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor.DashboardProcessor

class ConfirmMoveItemsDialog(view: BoxListView, selectedItems: List<DashboardItem>, destination: DashboardItem) {

    companion object {

        fun showDialog(boxListView: BoxListView, selectedItems: List<DashboardItem>, destination: DashboardItem) {
            val dialog = ConfirmMoveItemsDialog(boxListView, selectedItems, destination)
            dialog.showDialog()
        }

    }

    private val confirmDialog: ConfirmDialog = ConfirmDialog(
        view.context(),
        view.context().getString(R.string.move_items_title),
        view.context().getString(R.string.move_items_confirm_dialog_message, selectedItems.size.toString(), destination.name)
    )

    init {
        confirmDialog.setOnConfirmListener {
            DashboardProcessor.moveItems(view, destination, selectedItems)
            view.finishMoveWorkflow()
        }
    }

    private fun showDialog() {
        confirmDialog.showDialog()
    }

}