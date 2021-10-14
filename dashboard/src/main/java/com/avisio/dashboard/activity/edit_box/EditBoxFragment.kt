package com.avisio.dashboard.activity.edit_box

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

private const val BOX_OBJECT_KEY = "BOX_OBJECT_KEY"
private const val FRAGMENT_MODE_KEY = "FRAGMENT_MODE_KEY"

class EditBoxFragment : Fragment() {

    private lateinit var parcelableBox: ParcelableAvisioBox
    private var fragmentMode: EditBoxFragmentMode = EditBoxFragmentMode.CREATE_BOX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parcelableBox = it.getParcelable(BOX_OBJECT_KEY)!!
            fragmentMode = EditBoxFragmentMode.values()[it.getInt(FRAGMENT_MODE_KEY)]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_box, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(parcelableBox: ParcelableAvisioBox, fragmentMode: EditBoxFragmentMode) =
            EditBoxFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BOX_OBJECT_KEY, parcelableBox)
                    putInt(FRAGMENT_MODE_KEY, fragmentMode.ordinal)
                }
            }
    }
}