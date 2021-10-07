package com.avisio.dashboard.activity.box_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.AvisioBox
import com.avisio.dashboard.common.data.model.AvisioBoxViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class BoxListFragment : Fragment() {

    private lateinit var boxViewModel: AvisioBoxViewModel
    private lateinit var boxAdapter: AvisioBoxListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.box_list_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupView()
    }

    private fun setupView() {
        setupRecyclerView()
        setupBoxViewModel()
        setupFab()
    }

    private fun setupRecyclerView() {
        val boxListRecyclerView = view?.findViewById<RecyclerView>(R.id.box_list_recycler_view)
        boxAdapter = AvisioBoxListAdapter(AvisioBoxListAdapter.AvisioBoxDifference())
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
        view?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener { view ->
            startActivityForResult(Intent(view.context, CreateBoxActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            boxViewModel.insert(AvisioBox(name = data!!.getStringExtra("CREATE_REPLY")!!, createDate = Date(System.currentTimeMillis())))
        }
    }

}