package com.avisio.dashboard.usecase.crud_box.create_box

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.common.data.transfer.getCurrentFolder
import com.avisio.dashboard.common.data.transfer.isEditJobFromOuterScope
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.common.workflow.CRUD
//import com.avisio.dashboard.usecase.crud_box.common.BoxIconSelectionPopup
import com.avisio.dashboard.usecase.crud_box.common.ResetInputErrorTextWatcher
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.BoxFragmentStrategy
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditBoxFragment : Fragment() {

    companion object {
        const val BOX_CRUD_WORKFLOW = "BOX_CRUD_WORKFLOW"
    }

    internal lateinit var box: AvisioBox
    internal lateinit var currentFolder: DashboardItem
    internal var workflow: CRUD = CRUD.CREATE
    internal var isEditJobFromOuterScope = false

    lateinit var boxFragmentStrategy: BoxFragmentStrategy
    internal lateinit var boxNameTextInputLayout: TextInputLayout
    internal lateinit var nameInput: AppCompatEditText
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
            val fetchedFolder = it.getCurrentFolder()
            currentFolder = fetchedFolder ?: DashboardItem(id = -1, -1, DashboardItemType.FOLDER, "none", -1)
            isEditJobFromOuterScope = it.isEditJobFromOuterScope()
            workflow = CRUD.values()[it.getInt(BOX_CRUD_WORKFLOW)]
            boxFragmentStrategy = BoxFragmentStrategy.getStrategy(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_box, container, false)
    }

    override fun onStart() {
        super.onStart()
        setTitle()
        setupViews()
        fillBoxInformation()
    }

    private fun fillBoxInformation() {
        boxFragmentStrategy.fillBoxInformation()
    }

    private fun setupViews() {
        nameInput = requireView().findViewById(R.id.box_name_edit_text)!!
        boxNameTextInputLayout = requireView().findViewById(R.id.box_name_input_layout)
        nameInput.addTextChangedListener(ResetInputErrorTextWatcher(boxNameTextInputLayout))
        setupFab()
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_edit_box)?.setOnClickListener {
            boxFragmentStrategy.handleInput()
        }
    }

    private fun setTitle() {
        requireView().findViewById<Toolbar>(R.id.box_activity_app_bar).title = requireContext().getString(boxFragmentStrategy.titleId)
    }

}