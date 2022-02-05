package com.avisio.dashboard.usecase.crud_box.create_folder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.transfer.getCurrentFolder
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class EditFolderFragment : Fragment() {

    lateinit var dashboardItem: DashboardItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dashboardItem = it.getCurrentFolder()!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_folder, container, false)
    }

}