package com.avisio.dashboard.usecase.crud_box.read

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBoxViewModel
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.usecase.crud_box.create.CreateBoxActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = requireView().findViewById<Toolbar>(R.id.box_list_app_bar)
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        (requireActivity() as MainActivity).supportActionBar?.title = requireContext().getString(R.string.main_activity_app_bar_title)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        requireView().findViewById<Toolbar>(R.id.box_list_app_bar).title = requireContext().getString(R.string.main_activity_app_bar_title)
        setOnBackPressedDispatcher()
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
            boxAdapter.updateList(boxList)
        }
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_new_box)?.setOnClickListener { _ ->
            startActivity(Intent(context, CreateBoxActivity::class.java))
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.box_list_search)
        val searchView = SearchView((requireContext() as MainActivity).supportActionBar!!.themedContext)
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        searchItem.actionView = searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                boxAdapter.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                boxAdapter.getFilter().filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val searchView = requireView().findViewById<SearchView>(R.id.box_list_search)
                if(!searchView.isIconified) {
                    searchView.isIconified = true
                    searchView.onActionViewCollapsed()
                    return
                }
            }
        })
    }


}