package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor

import android.content.Intent
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.data.transfer.setCurrentFolder
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.move_items.BoxListView
import com.avisio.dashboard.usecase.crud_box.update.update_folder.EditFolderActivity

class FolderProcessor(private val item: DashboardItem, private val view: BoxListView) : DashboardProcessor() {

    override fun deleteItem() {
        view.getFolderRepository().deleteFolder(AvisioFolder(id = item.id))
    }

    override fun startEditItem() {
        val intent = Intent(view.context(), EditFolderActivity::class.java)
        intent.setCurrentFolder(item)
        view.context().startActivity(intent)
    }

}