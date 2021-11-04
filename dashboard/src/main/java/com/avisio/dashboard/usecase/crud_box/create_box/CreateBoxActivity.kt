package com.avisio.dashboard.usecase.crud_box.create_box

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.edit_box.EditBoxFragment
import com.avisio.dashboard.common.ui.edit_box.EditBoxFragmentMode
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.ui.edit_box.BoxIcon

class CreateBoxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_box)
        setSupportActionBar(findViewById(R.id.toolbar))
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(
            EditBoxFragment.BOX_OBJECT_KEY to ParcelableAvisioBox(-1, "", BoxIcon.DEFAULT.iconId),
            EditBoxFragment.FRAGMENT_MODE_KEY to EditBoxFragmentMode.CREATE_BOX.ordinal)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.create_fragment_container_view, EditBoxFragment::class.java, bundle)
        transaction.commit()
    }


}