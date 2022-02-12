package com.avisio.dashboard.usecase.crud_box.read.move_items

import com.avisio.dashboard.R

class MoveItemsWorkflow(private val boxListView: BoxListView) {

    fun initWorkflow() {
        boxListView.setAppBarTitle(R.string.move_items_title)
    }

    fun finishWorkflow() {
        boxListView.setAppBarTitle(R.string.main_activity_app_bar_title)
    }

}