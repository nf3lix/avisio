package com.avisio.dashboard.usecase.crud_box.read.bread_crumb

import com.avisio.dashboard.common.ui.breadcrump.BreadCrumb
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor.FolderProcessor
import com.avisio.dashboard.usecase.crud_box.read.move_items.BoxListView
import com.avisio.dashboard.usecase.crud_box.read.move_items.ConfirmMoveItemsDialog
import com.avisio.dashboard.usecase.crud_box.read.move_items.MoveItemsWorkflow
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BreadcrumbListener(private val view: BoxListView,
                         private val breadCrumb: DashboardBreadCrumb,
                         private val moveItemsWorkflow: MoveItemsWorkflow) : BreadCrumb.ElementClickListener {

    override fun onClick(index: Int) {
        val clickedBreadcrumbItem = breadCrumb.getDashboardItemFromBreadCrumbIndex(index)
        if(view.selectedItems().isEmpty()) {
            if(clickedBreadcrumbItem.id == -1L) {
                GlobalScope.launch {
                    FolderProcessor(null, view).openItem()
                }
            } else {
                GlobalScope.launch {
                    FolderProcessor(clickedBreadcrumbItem, view).openItem()
                }
            }
        } else if(isValidParentFolder(clickedBreadcrumbItem)) {
            ConfirmMoveItemsDialog.showDialog(view, view.selectedItems(), clickedBreadcrumbItem)
        }
    }

    private fun isValidParentFolder(item: DashboardItem): Boolean {
        return moveItemsWorkflow.isActive() && view.currentFolder() != null && view.currentFolder()?.id != item.id
    }

}