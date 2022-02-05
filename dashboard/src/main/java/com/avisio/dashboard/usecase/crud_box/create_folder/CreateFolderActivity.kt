package com.avisio.dashboard.usecase.crud_box.create_folder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.transfer.getCurrentFolder
import com.avisio.dashboard.common.data.transfer.setCurrentFolder
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType

class CreateFolderActivity : AppCompatActivity() {

    private lateinit var dashboardItem: DashboardItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fetchedFolder = intent.getCurrentFolder()
        dashboardItem = if(fetchedFolder == null) {
            DashboardItem(id = -1, -1, DashboardItemType.FOLDER, "none", -1)
        } else {
            intent.getCurrentFolder()!!
        }
        setContentView(R.layout.activity_create_folder)
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = Bundle()
        bundle.setCurrentFolder(dashboardItem)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.create_folder_fragment_container_view, EditFolderFragment::class.java, bundle)
        transaction.commit()
    }

}