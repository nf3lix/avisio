package com.avisio.dashboard.common.ui

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.training.activity.question.QuestionLearnFlexBox
import com.google.android.material.chip.Chip

class InputDialog(
    private val flexBox: QuestionLearnFlexBox,
    private val chip: Chip,
    view: ViewGroup?
) {

    private val dialogBuilder = AlertDialog.Builder(flexBox.context)
    private val viewInflated: View = LayoutInflater.from(flexBox.context).inflate(R.layout.dialog_cloze_text_answer, view, false)

    fun showDialog() {
        dialogBuilder
            .setTitle(flexBox.context.getString(R.string.create_card_answer_text_field_hint))
            .setView(viewInflated)
        setOnConfirmListener()
        setOnCancelListener()
        dialogBuilder.show()
    }

    private fun setOnConfirmListener() {
        dialogBuilder.setPositiveButton(flexBox.context.getText(R.string.confirm_dialog_confirm_default)) { _, _ ->
            chip.text = viewInflated.findViewById<AutoCompleteTextView>(R.id.cloze_answer_input).text.toString()
        }
    }

    private fun setOnCancelListener() {
        dialogBuilder.setNegativeButton(flexBox.context.getText(R.string.confirm_dialog_cancel_default)) { _, _ -> }
    }

}