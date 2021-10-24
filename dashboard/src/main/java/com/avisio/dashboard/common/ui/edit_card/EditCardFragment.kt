package com.avisio.dashboard.common.ui.edit_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.persistence.CardRepository
import com.avisio.dashboard.common.ui.ConfirmDialog

class EditCardFragment : Fragment(), ConfirmDialog.ConfirmDialogListener {

    companion object {
        const val FRAGMENT_MODE_KEY: String = "EDIT_CARD_FRAGMENT_MODE"
        const val BOX_OBJECT_KEY: String = "EDIT_CARD_BOX_OBJECT"
    }

    private var fragmentMode: EditCardFragmentMode = EditCardFragmentMode.CREATE_CARD
    private lateinit var parcelableBox: ParcelableAvisioBox

    private lateinit var cardRepository: CardRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardRepository = CardRepository(requireActivity().application)
        arguments?.let {
            fragmentMode = EditCardFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
            parcelableBox = it.getParcelable(BOX_OBJECT_KEY)!!
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