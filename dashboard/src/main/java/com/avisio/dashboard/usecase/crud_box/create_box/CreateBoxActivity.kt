package com.avisio.dashboard.usecase.crud_box.create_box

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.data.transfer.getCurrentFolder
import com.avisio.dashboard.common.data.transfer.setCurrentFolder
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType

class CreateBoxActivity : AppCompatActivity() {

    private lateinit var dashboardItem: DashboardItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fetchedFolder = intent.getCurrentFolder()
        dashboardItem = if(fetchedFolder == null) {
            DashboardItem(id = -1, -1, DashboardItemType.FOLDER, "none", -1)
        } else {
            intent.getCurrentFolder()!!
        }
        setContentView(R.layout.activity_create_box)
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(
            IntentKeys.BOX_OBJECT to ParcelableAvisioBox(-1, "", BoxIcon.DEFAULT.iconId),
            EditBoxFragment.BOX_CRUD_WORKFLOW to CRUD.CREATE.ordinal)
        bundle.setCurrentFolder(dashboardItem)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.create_fragment_container_view, EditBoxFragment::class.java, bundle)
        transaction.commit()
    }


}