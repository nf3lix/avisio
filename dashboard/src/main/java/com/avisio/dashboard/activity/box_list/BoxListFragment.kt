package com.avisio.dashboard.activity.box_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_activity.BoxActivityResultObserver
import com.avisio.dashboard.activity.create_box.CreateBoxResultObserver
import com.avisio.dashboard.common.data.model.AvisioBox
import com.avisio.dashboard.common.data.model.AvisioBoxViewModel
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxListFragment : Fragment(), AvisioBoxListAdapter.BoxListOnClickListener {

    private lateinit var boxViewModel: AvisioBoxViewModel
    private lateinit var boxAdapter: AvisioBoxListAdapter

    private lateinit var createBoxObserver: CreateBoxResultObserver
    private lateinit var boxActivityObserver: BoxActivityResultObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBoxObserver = CreateBoxResultObserver(this, requireActivity().activityResultRegistry)
        boxActivityObserver = BoxActivityResultObserver(this, requireActivity().activityResultRegistry)
        lifecycle.addObserver(createBoxObserver)
        lifecycle.addObserver(boxActivityObserver)
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
            createBoxObserver.createBox()
        }
    }

    fun newBoxReceived(avisioBox: AvisioBox) {
        boxViewModel.insert(avisioBox)
    }

    fun deleteBox(box: ParcelableAvisioBox) {
        val boxToDelete = boxAdapter.getBoxById(box.boxId)
        if(boxToDelete == null) {
            Toast.makeText(context, getString(R.string.delete_box_error_occurred), Toast.LENGTH_LONG).show()
            return
        }
        boxViewModel.deleteBox(boxToDelete)
    }

    override fun onClick(index: Int) {
        boxActivityObserver.startBoxActivity(boxAdapter.currentList[index])
    }

}