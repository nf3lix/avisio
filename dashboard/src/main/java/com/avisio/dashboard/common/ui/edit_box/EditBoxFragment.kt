package com.avisio.dashboard.common.ui.edit_box

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.box_list.BoxActivity
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.persistence.AvisioBoxRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class EditBoxFragment : Fragment() {

    companion object {
        const val BOX_OBJECT_KEY = "BOX_OBJECT_KEY"
        const val FRAGMENT_MODE_KEY = "FRAGMENT_MODE_KEY"
    }

    private lateinit var parcelableBox: ParcelableAvisioBox
    private var fragmentMode: EditBoxFragmentMode = EditBoxFragmentMode.CREATE_BOX

    private lateinit var nameInput: EditText
    private lateinit var iconImageView: ImageView
    private lateinit var boxRepository: AvisioBoxRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boxRepository = AvisioBoxRepository(requireActivity().application)
        arguments?.let {
            parcelableBox = it.getParcelable(BOX_OBJECT_KEY)!!
            fragmentMode = EditBoxFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_box, container, false)
    }

    override fun onStart() {
        super.onStart()
        nameInput = view?.findViewById(R.id.box_name_input)!!
        iconImageView = view?.findViewById(R.id.box_icon_imageview)!!
        setupFab()
        setupSelectIconButton()
        fillBoxInformation()
    }

    private fun fillBoxInformation() {
        if(fragmentMode == EditBoxFragmentMode.EDIT_BOX) {
            nameInput.setText(parcelableBox.boxName)
            updateBoxIcon(parcelableBox.boxIconId)
            return
        }
        updateBoxIcon(BoxIcon.DEFAULT.iconId)
    }

    private fun setupSelectIconButton() {
        view?.findViewById<Button>(R.id.select_icon_button)?.setOnClickListener {
            showSelectIconPopup()
        }
        iconImageView.setOnClickListener { showSelectIconPopup() }
    }

    private fun showSelectIconPopup() {
        val popup = BoxIconSelectionPopup.createPopup(this, nameInput)
        popup.setOnMenuItemClickListener { menuItem ->
            updateBoxIcon(menuItem.itemId)
            true
        }
        popup.show()
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_edit_box)?.setOnClickListener {
            handleFabClicked()
        }
    }

    private fun handleFabClicked() {
        val boxNameInput = nameInput.text
        when(TextUtils.isEmpty(boxNameInput)) {
            true -> {
                handleInvalidInput()
            }
            false -> {
                handleValidInput()
            }
        }
    }

    private fun handleInvalidInput() {
        Toast.makeText(context, getString(R.string.create_box_no_name_specified), Toast.LENGTH_LONG).show()
    }

    private fun handleValidInput() {
        when(fragmentMode) {
            EditBoxFragmentMode.CREATE_BOX -> {
                createNewBox()
            }
            EditBoxFragmentMode.EDIT_BOX -> {
                updateBox()
            }
        }
    }

    private fun createNewBox() {
        boxRepository.insert(
            AvisioBox(
            name = nameInput.text.toString(),
            createDate = Date(System.currentTimeMillis()),
            icon = BoxIcon.getBoxIcon(iconImageView.tag as Int)
        )
        )
        activity?.finish()
        Toast.makeText(requireContext(), "Box wurde erfolgreich erstellt", Toast.LENGTH_LONG).show()
    }

    private fun updateBox() {
        val updatedBox = getUpdatedBox()
        boxRepository.updateBox(getUpdatedBox())
        val intent = Intent(context, BoxActivity::class.java)
        intent.putExtra(BoxActivity.PARCELABLE_BOX_KEY, updatedBox)
        activity?.startActivity(intent)
        activity?.finish()
        Toast.makeText(requireContext(), "Box wurde erfolgreich geändert", Toast.LENGTH_LONG).show()
    }

    private fun updateBoxIcon(boxIconId: Int) {
        iconImageView.setImageResource(boxIconId)
        iconImageView.tag = boxIconId
    }

    private fun getUpdatedBox(): ParcelableAvisioBox {
        val updatedName = nameInput.text.toString()
        return ParcelableAvisioBox(parcelableBox.boxId, updatedName, iconImageView.tag as Int)
    }
}