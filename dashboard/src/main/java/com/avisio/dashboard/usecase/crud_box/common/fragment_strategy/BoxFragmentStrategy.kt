package com.avisio.dashboard.usecase.crud_box.common.fragment_strategy

import android.text.TextUtils
import com.avisio.dashboard.R
import com.avisio.dashboard.common.workflow.CRUD.CREATE
import com.avisio.dashboard.common.workflow.CRUD.UPDATE
import com.avisio.dashboard.usecase.crud_box.common.BoxNameExistsWarningDialog
import com.avisio.dashboard.usecase.crud_box.common.EditBoxFragment
import com.avisio.dashboard.usecase.crud_box.create_box.CreateBoxStrategy
import com.avisio.dashboard.usecase.crud_box.update.EditBoxStrategy

abstract class BoxFragmentStrategy(private val fragment: EditBoxFragment, val titleId: Int) {

    companion object {

        fun getStrategy(fragment: EditBoxFragment): BoxFragmentStrategy = when(fragment.workflow) {
            CREATE -> CreateBoxStrategy(fragment)
            UPDATE -> EditBoxStrategy(fragment)
        }

    }

    abstract fun fillBoxInformation()
    abstract fun saveBox()

    fun handleInput() {
        when(hasValidInput()) {
            true -> handleValidInput()
            false -> handleInvalidInput()
        }
    }

    private fun handleValidInput() {
        if(boxNameExists() && boxNameHasChanged()) {
            BoxNameExistsWarningDialog.showDialog(fragment)
            return
        }
        saveBox()
    }

    private fun boxNameExists(): Boolean {
        return fragment.nameInput.text.toString() in fragment.boxNameList
    }

    private fun boxNameHasChanged(): Boolean {
        return fragment.box.name != fragment.nameInput.text.toString()
    }

    private fun handleInvalidInput() {
        fragment.boxNameTextInputLayout.error = fragment.requireContext().getString(R.string.create_box_no_name_specified)
    }

    private fun hasValidInput(): Boolean {
        return !TextUtils.isEmpty(fragment.nameInput.text)
    }

}