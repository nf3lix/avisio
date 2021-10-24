package com.avisio.dashboard.common.ui.edit_card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_box.EditBoxFragment
import com.avisio.dashboard.common.ui.edit_box.EditBoxFragmentMode

class EditCardFragment : Fragment(), ConfirmDialog.ConfirmDialogListener {

    companion object {
        const val FRAGMENT_MODE_KEY: String = "EDIT_CARD_FRAGMENT_MODE"
    }

    private var fragmentMode: EditCardFragmentMode = EditCardFragmentMode.CREATE_CARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentMode = EditCardFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
        }
    }

    override fun onStart() {
        super.onStart()
        view?.findViewById<Spinner>(R.id.card_type_spinner)!!.adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, CardType.values())
        setOnBackPressedDispatcher()
    }

    private fun setOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })
    }

    fun onBackPressed() {
        val dialog = ConfirmDialog<Boolean>(
            requireContext(),
            this,
            getString(R.string.create_card_cancel_dialog_title),
            getString(R.string.create_card_cancel_dialog_question)
        )
        dialog.showDialog(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_card, container, false)
    }

    override fun onConfirm(data: Any) {
        activity?.finish()
    }


}