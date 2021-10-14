package com.avisio.dashboard.activity.edit_box

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

private const val BOX_OBJECT_KEY = "BOX_OBJECT_KEY"

class EditBoxFragment : Fragment() {

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parcelableBox = it.getParcelable(BOX_OBJECT_KEY)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_box, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditBoxFragment().apply {
                arguments = Bundle().apply {
                    putString(BOX_OBJECT_KEY, param1)
                }
            }
    }
}