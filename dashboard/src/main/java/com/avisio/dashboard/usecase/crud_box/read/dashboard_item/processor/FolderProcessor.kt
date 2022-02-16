package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor

import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.move_items.BoxListView

class FolderProcessor(private val item: DashboardItem, private val view: BoxListView) : DashboardProcessor() {

    override fun deleteItem() {
        view.getFolderRepository().deleteFolder(AvisioFolder(id = item.id))
    }

    override fun startEditItem() {
        TODO("Not yet implemented")
    }

}