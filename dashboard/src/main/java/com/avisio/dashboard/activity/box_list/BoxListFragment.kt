package com.avisio.dashboard.activity.box_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.create_box.CreateBoxResultObserver
import com.avisio.dashboard.common.data.model.AvisioBox
import com.avisio.dashboard.common.data.model.AvisioBoxViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxListFragment : Fragment(), AvisioBoxListAdapter.BoxListOnClickListener {

    private lateinit var boxViewModel: AvisioBoxViewModel
    private lateinit var boxAdapter: AvisioBoxListAdapter

    private lateinit var observer: CreateBoxResultObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = CreateBoxResultObserver(this, requireActivity().activityResultRegistry)
        lifecycle.addObserver(observer)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.box_list_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupView()
        setupFab()
    }

    private fun setupView() {
        setupRecyclerView()
        setupBoxViewModel()
    }

    private fun setupRecyclerView() {
        val boxListRecyclerView = view?.findViewById<RecyclerView>(R.id.box_list_recycler_view)
        boxAdapter = AvisioBoxListAdapter(AvisioBoxListAdapter.AvisioBoxDifference(), this)
        boxListRecyclerView?.adapter = boxAdapter
        boxListRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun setupBoxViewModel() {
        boxViewModel = ViewModelProvider(this).get(AvisioBoxViewModel::class.java)
        boxViewModel.getBoxList().observe(this) { boxList ->
            boxAdapter.submitList(boxList)
        }
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_new_box)?.setOnClickListener { _ ->
            observer.createBox()
        }
    }

    fun newBoxReceived(avisioBox: AvisioBox) {
        boxViewModel.insert(avisioBox)
    }

    override fun onClick(index: Int) {
        Log.d("test12345", boxAdapter.currentList[index].toString())
    }

}