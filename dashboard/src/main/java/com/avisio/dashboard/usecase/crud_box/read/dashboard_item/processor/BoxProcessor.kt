package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor

import android.content.Intent
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.transfer.setBoxObject
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.move_items.BoxListView
import com.avisio.dashboard.usecase.crud_box.update.update_box.EditBoxActivity

class BoxProcessor(private val item: DashboardItem, private val view: BoxListView) : DashboardProcessor() {

    override fun deleteItem() {
        view.getBoxRepository().deleteBox(AvisioBox(id = item.id))
    }

    override fun moveTo(destination: DashboardItem) {
        item.selected = false
        view.getBoxRepository().moveBox(AvisioBox(id = item.id), destination)
    }

    override fun startEditItem() {
        val box = AvisioBox(id = item.id, name = item.name!!, parentFolder = item.parentFolder)
        val intent = Intent(view.context(), EditBoxActivity::class.java)
        intent.setBoxObject(box)
        view.context().startActivity(intent)
    }

    override suspend fun openItem() {
        view.boxActivityObserver().startBoxActivity(item)
    }
}