package com.avisio.dashboard.usecase.crud_box.common.fragment_strategy

import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_box.create_folder.CreateFolderStrategy
import com.avisio.dashboard.usecase.crud_box.create_folder.EditFolderFragment
import com.avisio.dashboard.usecase.crud_box.update.update_folder.EditFolderStrategy

abstract class FolderFragmentStrategy(private val fragment: EditFolderFragment, val titleId: Int) {

    companion object {

        fun getStrategy(fragment: EditFolderFragment): FolderFragmentStrategy = when(fragment.workflow) {
            CRUD.CREATE -> CreateFolderStrategy(fragment)
            CRUD.UPDATE -> EditFolderStrategy(fragment)
        }

    }

}