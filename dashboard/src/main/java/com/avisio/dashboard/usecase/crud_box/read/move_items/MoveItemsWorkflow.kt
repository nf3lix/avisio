package com.avisio.dashboard.usecase.crud_box.read.move_items

import com.avisio.dashboard.R

class MoveItemsWorkflow(private val boxListView: BoxListView) {

    fun initMoveItemsWorkflow() {
        boxListView.setAppBarTitle(R.string.move_items_title)
    }

}