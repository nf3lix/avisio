package com.avisio.dashboard.usecase.crud_box.update.update_folder

import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.FolderFragmentStrategy
import com.avisio.dashboard.usecase.crud_box.create_folder.EditFolderFragment

class EditFolderStrategy(private val fragment: EditFolderFragment)
    : FolderFragmentStrategy(fragment, R.string.edit_folder) {

    override fun saveFolder() {
        fragment.folderRepository.updateFolderName(AvisioFolder(
                id = fragment.dashboardItem.id,
                name = fragment.nameInput.text.toString())
        )
        fragment.requireActivity().finish()
        Toast.makeText(fragment.requireContext(), R.string.edit_folder_success, Toast.LENGTH_LONG).show()
    }

    override fun fillFolderInformation() {
        fragment.nameInput.setText(fragment.dashboardItem.name)
    }

}