package com.avisio.dashboard.usecase.crud_box.common

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.common.data.transfer.setBoxObject
import com.avisio.dashboard.common.persistence.AvisioBoxRepository
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class EditBoxFragment : Fragment() {

    companion object {
        const val FRAGMENT_MODE_KEY = "FRAGMENT_MODE_KEY"
    }

    private lateinit var box: AvisioBox
    private var fragmentMode: EditBoxFragmentMode = EditBoxFragmentMode.CREATE_BOX

    private lateinit var boxNameTextInputLayout: TextInputLayout
    private lateinit var nameInput: AppCompatEditText
    private lateinit var iconImageView: ImageView
    private lateinit var boxRepository: AvisioBoxRepository
    private lateinit var boxNameList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boxRepository = AvisioBoxRepository(requireActivity().application)
        GlobalScope.launch {
            boxNameList = boxRepository.getBoxNameList()
        }
        arguments?.let {
            box = it.getBoxObject()!!
            fragmentMode = EditBoxFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_box, container, false)
    }

    override fun onStart() {
        super.onStart()
        nameInput = requireView().findViewById(R.id.box_name_edit_text)!!
        iconImageView = requireView().findViewById(R.id.box_icon_imageview)!!
        boxNameTextInputLayout = requireView().findViewById(R.id.box_name_input_layout)
        nameInput.setOnKeyListener { _, _, _ ->
            boxNameTextInputLayout.isErrorEnabled = false
            false
        }
        setupFab()
        setupSelectIconButton()
        fillBoxInformation()
    }

    private fun fillBoxInformation() {
        if(fragmentMode == EditBoxFragmentMode.EDIT_BOX) {
            nameInput.setText(box.name)
            updateBoxIcon(box.icon.iconId)
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
        boxNameTextInputLayout.error = getString(R.string.create_box_no_name_specified)
    }

    private fun handleValidInput() {
        if(nameInput.text.toString() in boxNameList &&
            (fragmentMode == EditBoxFragmentMode.CREATE_BOX || box.name != nameInput.text.toString())) {
            val dialog = ConfirmDialog(
                requireContext(),
                getString(R.string.create_box_duplicate_name_dialog_title),
                getString(R.string.create_box_duplicate_name_dialog_message)
            )
            dialog.setOnConfirmListener {
                saveChanges()
            }
            dialog.showDialog()
            return
        }
        saveChanges()
    }

    private fun saveChanges() {
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
            icon = BoxIcon.getBoxIcon(iconImageView.tag as Int))
        )
        requireActivity().finish()
        Toast.makeText(requireContext(), R.string.create_box_successful, Toast.LENGTH_LONG).show()
    }

    private fun updateBox() {
        val updatedBox = getUpdatedBox()
        boxRepository.updateBox(getUpdatedBox())
        val intent = Intent(context, BoxActivity::class.java)
        intent.setBoxObject(updatedBox)
        requireActivity().startActivity(intent)
        requireActivity().finish()
        Toast.makeText(requireContext(), R.string.edit_box_successful, Toast.LENGTH_LONG).show()
    }

    private fun updateBoxIcon(boxIconId: Int) {
        iconImageView.setImageResource(boxIconId)
        iconImageView.tag = boxIconId
    }

    private fun getUpdatedBox(): AvisioBox {
        val updatedName = nameInput.text.toString()
        return AvisioBox(
            id = box.id,
            name = updatedName,
            icon = BoxIcon.getBoxIcon(iconImageView.tag as Int)
        )
    }

}