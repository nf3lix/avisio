package com.avisio.dashboard.activity.box_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.AvisioBoxViewModel

class FirstFragment : Fragment() {

    private lateinit var boxViewModel: AvisioBoxViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupRecyclerView()
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    private fun setupRecyclerView() {
        val boxListRecyclerView = view?.findViewById<RecyclerView>(R.id.box_list_recycler_view)
        val boxAdapter = AvisioBoxListAdapter(AvisioBoxListAdapter.AvisioBoxDifference())
        boxListRecyclerView?.adapter = boxAdapter
        boxListRecyclerView?.layoutManager = LinearLayoutManager(context)
        boxViewModel = ViewModelProvider(this).get(AvisioBoxViewModel::class.java)

        boxViewModel.getBoxList().observe(this) { boxList ->
            boxAdapter.submitList(boxList)
        }
    }

}