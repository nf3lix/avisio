package com.avisio.dashboard.usecase.crud_box.read.move_items

import android.app.Activity
import android.content.Context
import android.widget.SearchView
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.common.persistence.folder.AvisioFolderRepository
import com.avisio.dashboard.usecase.crud_box.read.BoxActivityResultObserver
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

interface BoxListView {

    fun setAppBarTitle(titleId: Int)
    fun displayCancelWorkflowMenuItem(displayed: Boolean)
    fun displayCancelWorkflowButton(onClick: () -> Unit?)
    fun hideCancelWorkflowButton()
    fun updateItemList()

    fun showSelectedItemsActionButtons()
    fun hideSelectedItemsActionButtons()

    fun getFolderRepository(): AvisioFolderRepository
    fun getBoxRepository(): AvisioBoxRepository

    fun context(): Context
    fun activity(): Activity
    fun refreshBreadcrumb()
    fun setMoveWorkflowActive(active: Boolean)

    fun setCurrentFolder(folder: DashboardItem)
    fun toggleDeleteMenuItem()

    fun boxActivityObserver(): BoxActivityResultObserver

    fun searchView(): SearchView
    fun setFilterQuery(query: String)

}