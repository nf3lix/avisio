package com.avisio.dashboard.usecase.crud_box.create_folder

import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.FolderFragmentStrategy

class CreateFolderStrategy(private val fragment: EditFolderFragment)
    : FolderFragmentStrategy(fragment, R.string.fab_create_folder_label) {
}