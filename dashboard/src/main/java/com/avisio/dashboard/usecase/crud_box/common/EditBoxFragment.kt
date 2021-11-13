package com.avisio.dashboard.usecase.crud_box.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.common.persistence.AvisioBoxRepository
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.BoxFragmentStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditBoxFragment : Fragment() {

    companion object {
        const val BOX_CRUD_WORKFLOW = "BOX_CRUD_WORKFLOW"
    }

    internal lateinit var box: AvisioBox
    internal var workflow: CRUD = CRUD.CREATE

    lateinit var boxFragmentStrategy: BoxFragmentStrategy
    internal lateinit var boxNameTextInputLayout: TextInputLayout
    internal lateinit var nameInput: AppCompatEditText
    internal lateinit var iconImageView: ImageView
    internal lateinit var boxRepository: AvisioBoxRepository
    internal lateinit var boxNameList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boxRepository = AvisioBoxRepository(requireActivity().application)
        GlobalScope.launch {
            boxNameList = boxRepository.getBoxNameList()
        }
        arguments?.let {
            box = it.getBoxObject()!!
            workflow = CRUD.values()[it.getInt(BOX_CRUD_WORKFLOW)]
            boxFragmentStrategy = BoxFragmentStrategy.getStrategy(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_box, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupViews()
        fillBoxInformation()
    }

    private fun fillBoxInformation() {
        boxFragmentStrategy.fillBoxInformation()
    }

    private fun setupViews() {
        nameInput = requireView().findViewById(R.id.box_name_edit_text)!!
        iconImageView = requireView().findViewById(R.id.box_icon_imageview)!!
        boxNameTextInputLayout = requireView().findViewById(R.id.box_name_input_layout)
        nameInput.addTextChangedListener(ResetInputErrorTextWatcher(boxNameTextInputLayout))
        setupFab()
        setupSelectIconButton()
    }

    private fun setupSelectIconButton() {
        val onClickListener = View.OnClickListener { showSelectIconPopup() }
        requireView().findViewById<Button>(R.id.select_icon_button)?.setOnClickListener(onClickListener)
        iconImageView.setOnClickListener(onClickListener)
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_edit_box)?.setOnClickListener {
            boxFragmentStrategy.handleInput()
        }
    }

    private fun showSelectIconPopup() {
        BoxIconSelectionPopup.showPopup(this, nameInput)
    }

    fun updateBoxIcon(boxIconId: Int) {
        iconImageView.setImageResource(boxIconId)
        iconImageView.tag = boxIconId
    }

}