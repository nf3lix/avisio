package com.avisio.dashboard.usecase.crud_box.read.bread_crumb

import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType

class DashboardBreadCrumb {

    companion object {

        private val HOME = DashboardItem(id = -1, parentFolder = -1, type = DashboardItemType.FOLDER, icon = -1, name = "home")

        fun updateBreadCrumb(adapter: DashboardBreadCrumbAdapter, currentItem: DashboardItem?, allItems: List<DashboardItem>) {
            if(currentItem != null) {
                val parentFolders = getParentFoldersIteratively(currentItem, allItems)
                adapter.setElements(parentFolders)
            } else {
                setToHomeFolder(adapter)
            }
        }

        fun setToHomeFolder(adapter: DashboardBreadCrumbAdapter) {
            adapter.setElements(arrayListOf(HOME))
        }

        private fun getParentFoldersIteratively(item: DashboardItem, allItems: List<DashboardItem>): List<DashboardItem> {
            val folderChain = arrayListOf<DashboardItem>()
            folderChain.add(item)
            var currentParentFolder = getDirectParentFolder(item, allItems)
            while(currentParentFolder != null) {
                folderChain.add(0, currentParentFolder)
                currentParentFolder = getDirectParentFolder(currentParentFolder, allItems)
            }
            folderChain.add(0, HOME)
            return folderChain
        }

        private fun getDirectParentFolder(currentItem: DashboardItem, allItems: List<DashboardItem>): DashboardItem? {
            for(item in allItems) {
                if(item.type == DashboardItemType.FOLDER && item.id == currentItem.parentFolder) {
                    return item
                }
            }
            return null
        }

    }

}