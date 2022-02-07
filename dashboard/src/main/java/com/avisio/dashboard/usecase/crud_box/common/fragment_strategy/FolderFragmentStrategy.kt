package com.avisio.dashboard.usecase.crud_box.common.fragment_strategy

import android.text.TextUtils
import com.avisio.dashboard.R
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

    abstract fun saveFolder()
    abstract fun fillFolderInformation()

    fun handleInput() {
        when(hasValidInput()) {
            true -> {
                handleValidInput()
            }
            false -> {
                handleInvalidInput()
            }
        }
    }

    private fun handleValidInput() {
        saveFolder()
    }

    private fun handleInvalidInput() {
        fragment.folderNameTextInputLayout.error = fragment.requireContext().getString(R.string.no_folder_name_specified)
    }

    private fun hasValidInput(): Boolean {
        return !TextUtils.isEmpty(fragment.nameInput.text)
    }

}