package com.avisio.dashboard.usecase.crud_box.update.update_box

import android.content.Intent
import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.transfer.setBoxObject
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import com.avisio.dashboard.usecase.crud_box.create_box.EditBoxFragment
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.BoxFragmentStrategy
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity

class EditBoxStrategy(private val fragment: EditBoxFragment) : BoxFragmentStrategy(fragment, R.string.box_activity_menu_edit) {

    override fun fillBoxInformation() {
        fragment.nameInput.setText(fragment.box.name)
        fragment.updateBoxIcon(fragment.box.icon.iconId)
    }

    override fun saveBox() {
        val updatedBox = getUpdatedBox()
        fragment.boxRepository.updateBox(getUpdatedBox())
        val intent = Intent(fragment.requireContext(), BoxActivity::class.java)
        intent.setBoxObject(updatedBox)
        fragment.requireActivity().startActivity(intent)
        fragment.requireActivity().finish()
        Toast.makeText(fragment.requireContext(), R.string.edit_box_successful, Toast.LENGTH_LONG).show()
    }

    private fun getUpdatedBox(): AvisioBox {
        val updatedName = fragment.nameInput.text.toString()
        return AvisioBox(
            id = fragment.box.id,
            name = updatedName,
            icon = BoxIcon.getBoxIcon(fragment.iconImageView.tag as Int)
        )
    }

}