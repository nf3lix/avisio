package com.avisio.dashboard.usecase.crud_box.update.update_folder

import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.FolderFragmentStrategy
import com.avisio.dashboard.usecase.crud_box.create_folder.EditFolderFragment

class EditFolderStrategy(private val fragment: EditFolderFragment)
    : FolderFragmentStrategy(fragment, R.string.edit_folder) {

    override fun saveFolder() {
    }

}