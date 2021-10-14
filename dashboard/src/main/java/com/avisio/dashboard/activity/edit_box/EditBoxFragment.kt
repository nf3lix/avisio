package com.avisio.dashboard.activity.edit_box

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.AvisioBox
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox
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

    private lateinit var boxDao: AvisioBoxRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boxDao = AvisioBoxRepository(requireActivity().application)
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
        setupFab()
        fillBoxInformation()
    }

    private fun fillBoxInformation() {
        view?.findViewById<EditText>(R.id.fragment_box_name_input)?.setText(parcelableBox.boxName)
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_edit_box)?.setOnClickListener {
            handleFabClicked()
        }
    }

    private fun handleFabClicked() {
        val boxNameInput = view?.findViewById<EditText>(R.id.fragment_box_name_input)?.text
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
        boxDao.insert(AvisioBox(name = view?.findViewById<EditText>(R.id.fragment_box_name_input)?.text.toString(), createDate = Date(System.currentTimeMillis())))
        activity?.finish()
    }

    private fun updateBox() {
        val updatedBox = getUpdatedBox()
        if(boxChanged(updatedBox)) {
            boxDao.updateBox(getUpdatedBox())
        }
        activity?.finish()
    }

    private fun getUpdatedBox(): ParcelableAvisioBox {
        val updatedName = view?.findViewById<EditText>(R.id.fragment_box_name_input)?.text.toString()
        return ParcelableAvisioBox(parcelableBox.boxId, updatedName)
    }

    private fun boxChanged(updatedBox: ParcelableAvisioBox): Boolean {
        return parcelableBox.boxName != updatedBox.boxName
    }

}