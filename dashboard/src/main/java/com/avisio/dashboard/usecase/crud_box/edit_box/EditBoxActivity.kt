package com.avisio.dashboard.usecase.crud_box.edit_box

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.box_list.BoxActivity
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.ui.edit_box.EditBoxFragment
import com.avisio.dashboard.common.ui.edit_box.EditBoxFragmentMode

class EditBoxActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parcelableBox = intent.getParcelableExtra(BoxActivity.PARCELABLE_BOX_KEY)!!
        setContentView(R.layout.activity_edit_box)
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(
            EditBoxFragment.BOX_OBJECT_KEY to parcelableBox,
            EditBoxFragment.FRAGMENT_MODE_KEY to EditBoxFragmentMode.EDIT_BOX.ordinal)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.edit_fragment_container_view, EditBoxFragment::class.java, bundle)
        transaction.commit()
    }

}