package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment

class DeleteImagesConfirmDialog(private val fragment: EditCardFragment) {

    companion object {

        fun showDialog(fragment: EditCardFragment) {
            val dialog = DeleteImagesConfirmDialog(fragment)
            dialog.showDialog()
        }

    }

    private val confirmDialog: ConfirmDialog = ConfirmDialog(
        fragment.requireContext(),
        fragment.requireContext().getString(R.string.edit_card_set_type_remove_images_title),
        fragment.requireContext().getString(R.string.edit_card_set_type_remove_images_message),
    )

    init {
        setOnConfirmListener()
        setOnCancelListener()
    }

    private fun setOnConfirmListener() {
        confirmDialog.setOnConfirmListener {
            fragment.removeAllImages()
            fragment.onFlexboxInputChanged(fragment.questionInput)
            fragment.onFlexboxInputChanged(fragment.answerInput)
            fragment.onCardTypeSet(fragment.getSelectedCardType())
        }
    }

    private fun setOnCancelListener() {
        confirmDialog.setOnCancelListener {
            fragment.onCardTypeSet(CardType.STANDARD)
        }
    }

    private fun showDialog() {
        confirmDialog.showDialog()
    }

}