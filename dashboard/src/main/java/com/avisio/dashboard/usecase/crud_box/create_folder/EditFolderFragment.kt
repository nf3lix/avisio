package com.avisio.dashboard.usecase.crud_box.create_folder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.transfer.getCRUDWorkflow
import com.avisio.dashboard.common.data.transfer.getCurrentFolder
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_box.common.fragment_strategy.FolderFragmentStrategy
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class EditFolderFragment : Fragment() {

    internal lateinit var dashboardItem: DashboardItem
    internal var workflow: CRUD = CRUD.CREATE
    lateinit var folderFragmentStrategy: FolderFragmentStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dashboardItem = it.getCurrentFolder()!!
            workflow = it.getCRUDWorkflow()
            folderFragmentStrategy = FolderFragmentStrategy.getStrategy(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_folder, container, false)
    }

    override fun onStart() {
        super.onStart()
        setTitle()
    }

    private fun setTitle() {
        requireView().findViewById<Toolbar>(R.id.folder_activity_app_bar).title = requireContext().getString(folderFragmentStrategy.titleId)
    }

}