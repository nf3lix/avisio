package com.avisio.dashboard.usecase.crud_box.read

import android.content.Intent
import android.os.Bundle
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
import com.avisio.dashboard.common.data.model.box.DashboardItemViewModel
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.usecase.crud_box.create.CreateBoxActivity
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxListFragment : Fragment(), DashboardItemListAdapter.DashboardItemOnClickListener {

    private lateinit var dashboardItemViewModel: DashboardItemViewModel
    private lateinit var dashboardItemAdapter: DashboardItemListAdapter

    private lateinit var boxActivityObserver: BoxActivityResultObserver

    private var currentFolder: Long? = null
    private var currentFolderItem: DashboardItem? = null
    private var allItems = listOf<DashboardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boxActivityObserver = BoxActivityResultObserver(this, requireActivity().activityResultRegistry, requireActivity().application)
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
        val dashboardItemRecyclerView = view?.findViewById<RecyclerView>(R.id.box_list_recycler_view)
        dashboardItemAdapter = DashboardItemListAdapter(DashboardItemListAdapter.DashboardItemDifference(), this)
        dashboardItemRecyclerView?.adapter = dashboardItemAdapter
        dashboardItemRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun setupBoxViewModel() {
        dashboardItemViewModel = ViewModelProvider(this).get(DashboardItemViewModel::class.java)
        attachDashboardItemAdapter()
    }

    private fun attachDashboardItemAdapter() {
        dashboardItemViewModel.getDashboardItemList().observe(this) { itemList ->
            allItems = itemList
            val filteredList = filterItemsOfCurrentFolder(itemList)
            dashboardItemAdapter.updateList(filteredList)
        }
    }

    private fun filterItemsOfCurrentFolder(itemList: List<DashboardItem>): List<DashboardItem> {
        val list = arrayListOf<DashboardItem>()
        for(item in itemList) {
            if(item.parentFolder == currentFolder) list.add(item)
        }
        return list
    }

    private fun setupFab() {
        view?.findViewById<FloatingActionButton>(R.id.fab_new_box)?.setOnClickListener { _ ->
            startActivity(Intent(context, CreateBoxActivity::class.java))
        }
    }

    fun deleteBox(box: ParcelableAvisioBox) {
        val boxToDelete = dashboardItemAdapter.getDashboardItemById(box.boxId)
        if(boxToDelete == null) {
            Toast.makeText(context, getString(R.string.delete_box_error_occurred), Toast.LENGTH_LONG).show()
            return
        }
        dashboardItemViewModel.deleteDashboardItem(boxToDelete)
    }

    override suspend fun onClick(index: Int) {
        val clickedItem = dashboardItemAdapter.currentList[index]
        return when(clickedItem.type) {
            DashboardItemType.BOX -> {
                boxActivityObserver.startBoxActivity(clickedItem)
            }
            DashboardItemType.FOLDER -> {
                openFolder(clickedItem)
            }
        }
    }

    private fun openFolder(item: DashboardItem?) {
        requireActivity().runOnUiThread {
            currentFolder = item?.id
            currentFolderItem = item
            dashboardItemAdapter.updateList(filterItemsOfCurrentFolder(allItems))
        }
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
                dashboardItemAdapter.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dashboardItemAdapter.getFilter().filter(newText)
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
                    closeSearchView(searchView)
                    return
                }
                openParentFolder()
            }
        })
    }

    private fun closeSearchView(searchView: SearchView) {
        searchView.isIconified = true
        searchView.onActionViewCollapsed()
    }

    private fun openParentFolder() {
        val parentFolder = getParentFolder()
        openFolder(parentFolder)
    }

    private fun getParentFolder(): DashboardItem? {
        if(currentFolderItem == null) return null
        val parentFolderId = currentFolderItem!!.parentFolder
        for(item in allItems) {
            if(item.id == parentFolderId && item.type == DashboardItemType.FOLDER) {
                return item
            }
        }
        return null
    }

}