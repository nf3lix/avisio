package com.avisio.dashboard.activity.edit_box

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class EditBoxFragment : Fragment() {

    companion object {
        const val BOX_OBJECT_KEY = "BOX_OBJECT_KEY"
        const val FRAGMENT_MODE_KEY = "FRAGMENT_MODE_KEY"
    }

    private lateinit var parcelableBox: ParcelableAvisioBox
    private var fragmentMode: EditBoxFragmentMode = EditBoxFragmentMode.CREATE_BOX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parcelableBox = it.getParcelable(BOX_OBJECT_KEY)!!
            fragmentMode = EditBoxFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_box, container, false)
    }

    override fun onStart() {
        super.onStart()
        fillBoxInformation()
    }

    private fun fillBoxInformation() {
        view?.findViewById<EditText>(R.id.fragment_box_name_input)?.setText(parcelableBox.boxName)
    }

}