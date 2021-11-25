package com.avisio.dashboard.usecase.crud_box.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.data.transfer.getBoxObject
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.common.workflow.CRUD.*
import com.avisio.dashboard.usecase.crud_box.common.EditBoxFragment

class EditBoxActivity : AppCompatActivity() {

    private lateinit var box: AvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        box = intent.getBoxObject()!!
        setContentView(R.layout.activity_edit_box)
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(
            IntentKeys.BOX_OBJECT to ParcelableAvisioBox.createFromEntity(box),
            EditBoxFragment.BOX_CRUD_WORKFLOW to UPDATE.ordinal)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.edit_fragment_container_view, EditBoxFragment::class.java, bundle)
        transaction.commit()
    }

}