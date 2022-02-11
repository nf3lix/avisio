package com.avisio.dashboard.usecase.crud_box.read.bread_crumb

import com.avisio.dashboard.usecase.crud_box.read.BoxListFragment
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType

class DashboardBreadCrumb(private val fragment: BoxListFragment, private val adapter: DashboardBreadCrumbAdapter) {

    private var currentPath = arrayListOf<DashboardItem>()

    companion object {
        private val HOME = DashboardItem(id = -1, parentFolder = -1, type = DashboardItemType.FOLDER, icon = -1, name = "home")
    }

    fun updateBreadCrumb(currentItem: DashboardItem?) {
        if(currentItem != null) {
            val parentFolders = getParentFoldersIteratively(currentItem)
            adapter.setElements(parentFolders)
            currentPath = parentFolders
        } else {
            setToHomeFolder()
        }
    }

    fun setToHomeFolder() {
        adapter.setElements(arrayListOf(HOME))
        currentPath = arrayListOf(HOME)
    }

    fun getDashboardItemFromBreadCrumbIndex(index: Int): DashboardItem {
        return currentPath[index]
    }

    private fun getParentFoldersIteratively(item: DashboardItem): ArrayList<DashboardItem> {
        val folderChain = arrayListOf<DashboardItem>()
        folderChain.add(item)
        var currentParentFolder = getDirectParentFolder(item)
        while(currentParentFolder != null) {
            folderChain.add(0, currentParentFolder)
            currentParentFolder = getDirectParentFolder(currentParentFolder)
        }
        folderChain.add(0, HOME)
        return folderChain
    }

    private fun getDirectParentFolder(currentItem: DashboardItem): DashboardItem? {
        for(item in fragment.allItems) {
            if(item.type == DashboardItemType.FOLDER && item.id == currentItem.parentFolder) {
                return item
            }
        }
        return null
    }

}