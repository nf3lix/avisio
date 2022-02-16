package com.avisio.dashboard.usecase.crud_box.read.move_items

import android.content.Context
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.common.persistence.folder.AvisioFolderRepository

interface BoxListView {

    fun setAppBarTitle(titleId: Int)
    fun displayCancelWorkflowMenuItem(displayed: Boolean)

    fun displayCancelWorkflowButton(onClick: () -> Unit?)
    fun hideCancelWorkflowButton()

    fun showSelectedItemsActionButtons()
    fun hideSelectedItemsActionButtons()

    fun updateItemList()

    fun setMoveWorkflowActive(active: Boolean)

    fun getFolderRepository(): AvisioFolderRepository
    fun getBoxRepository(): AvisioBoxRepository

    fun context(): Context

}