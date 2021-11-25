package com.avisio.dashboard.usecase.crud_box.create

import android.widget.Toast
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import com.avisio.dashboard.usecase.crud_box.common.EditBoxFragment
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.BoxFragmentStrategy
import java.util.*

class CreateBoxStrategy(private val fragment: EditBoxFragment) : BoxFragmentStrategy(fragment, R.string.create_box_app_bar_title) {

    override fun fillBoxInformation() {
        fragment.updateBoxIcon(BoxIcon.DEFAULT.iconId)
    }

    override fun saveBox() {
        fragment.boxRepository.insert(getBoxToCreate())
        fragment.requireActivity().finish()
        Toast.makeText(fragment.requireContext(), R.string.create_box_successful, Toast.LENGTH_LONG).show()
    }

    private fun getBoxToCreate(): AvisioBox {
        return AvisioBox(
            name = fragment.nameInput.text.toString(),
            createDate = Date(System.currentTimeMillis()),
            icon = BoxIcon.getBoxIcon(fragment.iconImageView.tag as Int)
        )
    }

}