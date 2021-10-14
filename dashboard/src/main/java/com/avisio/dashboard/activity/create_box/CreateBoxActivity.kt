package com.avisio.dashboard.activity.create_box

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.edit_box.EditBoxFragment
import com.avisio.dashboard.activity.edit_box.EditBoxFragmentMode
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class CreateBoxActivity : AppCompatActivity() {

    companion object {
        const val BOX_NAME_OBSERVER_REPLY: String = "BOX_NAME_REPLY"
    }

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
            EditBoxFragment.BOX_OBJECT_KEY to ParcelableAvisioBox(-1, ""),
            EditBoxFragment.FRAGMENT_MODE_KEY to EditBoxFragmentMode.CREATE_BOX.ordinal)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<EditBoxFragment>(R.id.fragment_container_view, args = bundle)
        }
    }


}