package com.avisio.dashboard.usecase.crud_box.read.move_items

import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.read.BoxListWorkflow

class MoveItemsWorkflow(private val boxListView: BoxListView) : BoxListWorkflow() {

    private var isActive = false

    fun initWorkflow() {
        isActive = true
        boxListView.setAppBarTitle(R.string.move_items_title)
        boxListView.displayCancelWorkflowMenuItem(true)
        boxListView.hideSelectedItemsActionButtons()
        boxListView.displayCancelWorkflowButton {
            finishWorkflow()
        }
    }

    fun finishWorkflow() {
        isActive = false
        boxListView.setAppBarTitle(R.string.main_activity_app_bar_title)
        boxListView.displayCancelWorkflowMenuItem(false)
        boxListView.hideCancelWorkflowButton()
    }

    override fun getDisplayedMenuItems(): Map<Int, Boolean> {
        return mapOf(
            R.id.action_stop_workflow to true,
            R.id.dashboard_list_search to false,
            R.id.action_settings to false,
            R.id.action_rename_folder to false,
            R.id.action_delete_folder to false,
        )
    }

    fun isActive(): Boolean {
        return isActive
    }

}