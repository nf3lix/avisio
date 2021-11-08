package com.avisio.dashboard.usecase.crud_box.box_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.ui.ConfirmDialog

class ConfirmDeleteBoxDialog(activity: BoxActivity, boxToDelete: ParcelableAvisioBox) {

    companion object {

        fun showDialog(activity: BoxActivity, boxToDelete: ParcelableAvisioBox) {
            val dialog = ConfirmDeleteBoxDialog(activity, boxToDelete)
            dialog.showDialog()
        }

    }

    private val confirmDialog: ConfirmDialog = ConfirmDialog(
        activity,
        activity.baseContext.getString(R.string.delete_box_confirm_dialog_title),
        activity.baseContext.getString(R.string.delete_box_confirm_dialog_message)
    )

    init {
        confirmDialog.setOnConfirmListener {
            val resultIntent = Intent()
            resultIntent.putExtra(BoxActivity.BOX_DELETE_OBSERVER_REPLY, boxToDelete)
            activity.setResult(AppCompatActivity.RESULT_OK, resultIntent)
            activity.finish()
        }
    }

    private fun showDialog() {
        confirmDialog.showDialog()
    }

}