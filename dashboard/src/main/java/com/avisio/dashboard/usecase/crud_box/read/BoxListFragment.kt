package com.avisio.dashboard.usecase.crud_box.read

import android.content.Intent
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
import com.avisio.dashboard.usecase.crud_box.create.CreateBoxActivity
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.AvisioBoxViewModel
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxListFragment : Fragment(), AvisioBoxListAdapter.BoxListOnClickListener {

    private lateinit var boxViewModel: AvisioBoxViewModel
    private lateinit var boxAdapter: AvisioBoxListAdapter

    private lateinit var boxActivityObserver: BoxActivityResultObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boxActivityObserver = BoxActivityResultObserver(this, requireActivity().activityResultRegistry)
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
            startActivity(Intent(context, CreateBoxActivity::class.java))
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