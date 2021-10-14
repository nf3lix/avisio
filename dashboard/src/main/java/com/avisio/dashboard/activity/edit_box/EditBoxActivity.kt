package com.avisio.dashboard.activity.edit_box

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox
import com.avisio.dashboard.common.persistence.AvisioBoxRepository

class EditBoxActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox
    private lateinit var boxDao: AvisioBoxRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_box)
        if(savedInstanceState == null) {
            val bundle = bundleOf(
                EditBoxFragment.BOX_OBJECT_KEY to ParcelableAvisioBox(1, "TEST_NAME"),
                EditBoxFragment.FRAGMENT_MODE_KEY to EditBoxFragmentMode.EDIT_BOX.ordinal)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<EditBoxFragment>(R.id.fragment_container_view, args = bundle)
            }
        }
    }



}