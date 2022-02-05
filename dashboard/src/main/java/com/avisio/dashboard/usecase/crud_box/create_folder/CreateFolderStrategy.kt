package com.avisio.dashboard.usecase.crud_box.create_folder

import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.FolderFragmentStrategy

class CreateFolderStrategy(private val fragment: EditFolderFragment)
    : FolderFragmentStrategy(fragment, R.string.fab_create_folder_label) {

    override fun saveFolder() {
        fragment.folderRepository.createFolder(getFolderToCreate())
        fragment.requireActivity().finish()
        Toast.makeText(fragment.requireContext(), R.string.create_folder_success, Toast.LENGTH_LONG).show()
    }

    private fun getFolderToCreate(): AvisioFolder {
        if(fragment.dashboardItem.parentFolder == -1L) {
            return AvisioFolder(name = fragment.nameInput.text.toString())
        }
        return AvisioFolder(
            name = fragment.nameInput.text.toString(),
            parentFolder = fragment.dashboardItem.id
        )
    }

}