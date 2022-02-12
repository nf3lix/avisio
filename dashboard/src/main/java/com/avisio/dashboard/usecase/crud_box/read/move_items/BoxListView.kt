package com.avisio.dashboard.usecase.crud_box.read.move_items

interface BoxListView {

    fun setAppBarTitle(titleId: Int)
    fun displayCancelWorkflowMenuItem(displayed: Boolean)

    fun displayCancelWorkflowButton(onClick: () -> Unit?)
    fun hideCancelWorkflowButton()

    fun showSelectedItemsActionButtons()
    fun hideSelectedItemsActionButtons()

    fun updateItemList()

    fun setMoveWorkflowActive(active: Boolean)

}