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
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.data.model.box.DashboardItemViewModel
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.setBoxObject
import com.avisio.dashboard.common.data.transfer.setCurrentFolder
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.common.persistence.folder.AvisioFolderRepository
import com.avisio.dashboard.common.ui.breadcrump.BreadCrumb
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.usecase.crud_box.common.ConfirmDeleteSelectedItemsDialog
import com.avisio.dashboard.usecase.crud_box.create_box.CreateBoxActivity
import com.avisio.dashboard.usecase.crud_box.create_folder.CreateFolderActivity
import com.avisio.dashboard.usecase.crud_box.delete_folder.ConfirmDeleteFolderDialog
import com.avisio.dashboard.usecase.crud_box.read.bread_crumb.DashboardBreadCrumb
import com.avisio.dashboard.usecase.crud_box.read.bread_crumb.DashboardBreadCrumbAdapter
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import com.avisio.dashboard.usecase.crud_box.update.update_box.EditBoxActivity
import com.avisio.dashboard.usecase.crud_box.update.update_folder.EditFolderActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxListFragment : Fragment(), DashboardItemListAdapter.DashboardItemOnClickListener {

    private lateinit var dashboardItemViewModel: DashboardItemViewModel
    private lateinit var dashboardItemAdapter: DashboardItemListAdapter
    private lateinit var folderRepository: AvisioFolderRepository
    private lateinit var boxRepository: AvisioBoxRepository

    private lateinit var breadCrumb: BreadCrumb
    private lateinit var breadCrumbAdapter: DashboardBreadCrumbAdapter
    private lateinit var dashboardBreadCrumb: DashboardBreadCrumb

    private lateinit var boxActivityObserver: BoxActivityResultObserver

    private var currentFolder: DashboardItem? = null
    internal var allItems = listOf<DashboardItem>()

    private var fabMenuShown = false
    private lateinit var menu: Menu

    private lateinit var editSelectedItemButton: ExtendedFloatingActionButton
    private lateinit var deleteSelectedItemsButton: ExtendedFloatingActionButton
    private lateinit var moveSelectedItemsButton: ExtendedFloatingActionButton

    private lateinit var fabCreateBox: ExtendedFloatingActionButton
    private lateinit var fabCreateFolder: ExtendedFloatingActionButton
    private lateinit var fabExpand: FloatingActionButton

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
        fabCreateBox = requireView().findViewById(R.id.fab_new_box)
        fabCreateFolder = requireView().findViewById(R.id.fab_new_folder)
        fabExpand = requireView().findViewById(R.id.fab_expand)
        folderRepository = AvisioFolderRepository(requireActivity().application)
        boxRepository = AvisioBoxRepository(requireActivity().application)
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
        setupBreadCrumb()
        setupBoxViewModel()
        closeFabMenu()
        setupSelectedItemsActionButtons()
    }

    private fun setupRecyclerView() {
        val dashboardItemRecyclerView = view?.findViewById<RecyclerView>(R.id.box_list_recycler_view)
        dashboardItemAdapter = DashboardItemListAdapter(DashboardItemListAdapter.DashboardItemDifference(), this)
        dashboardItemRecyclerView?.adapter = dashboardItemAdapter
        dashboardItemRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun setupBreadCrumb() {
        breadCrumb = requireView().findViewById(R.id.breadCrumb)
        breadCrumbAdapter = DashboardBreadCrumbAdapter()
        breadCrumbAdapter.setBreadCrumb(breadCrumb)
        dashboardBreadCrumb = DashboardBreadCrumb(this, breadCrumbAdapter)
        dashboardBreadCrumb.updateBreadCrumb(currentFolder)
        breadCrumb.setOnBreadCrumbElementClickListener { index ->
            if(dashboardItemAdapter.selectedItems().isEmpty()) {
                val clickedItem = dashboardBreadCrumb.getDashboardItemFromBreadCrumbIndex(index)
                if(clickedItem.id == -1L) {
                    openFolder(null)
                } else {
                    openFolder(clickedItem)
                }
            }
        }
    }

    private fun setupBoxViewModel() {
        dashboardItemViewModel = ViewModelProvider(this).get(DashboardItemViewModel::class.java)
        attachDashboardItemAdapter()
    }

    private fun attachDashboardItemAdapter() {
        dashboardItemViewModel.getDashboardItemList().observe(this) { itemList ->
            allItems = itemList
            val filteredList = filterItemsOfCurrentFolder(itemList)
            dashboardItemAdapter.updateAllItemList(itemList)
            dashboardItemAdapter.updateList(filteredList)
        }
    }

    private fun filterItemsOfCurrentFolder(itemList: List<DashboardItem>): List<DashboardItem> {
        val list = arrayListOf<DashboardItem>()
        for(item in itemList) {
            if(item.parentFolder == currentFolder?.id) list.add(item)
        }
        return list
    }

    private fun setupFab() {
        fabExpand.setOnClickListener {
            toggleFabMenu()
        }

        fabCreateBox.setOnClickListener {
            val intent = Intent(context, CreateBoxActivity::class.java)
            intent.setCurrentFolder(currentFolder)
            startActivity(intent)
        }

        fabCreateFolder.setOnClickListener {
            val intent = Intent(context, CreateFolderActivity::class.java)
            intent.setCurrentFolder(currentFolder)
            startActivity(Intent(intent))
        }

    }

    private fun toggleFabMenu() {
        if(!fabMenuShown) {
            showFabMenu()
        } else {
            closeFabMenu()
        }
    }

    private fun showFabMenu() {
        fabMenuShown = true
        fabExpand.setImageResource(R.drawable.ic_close)
        fabCreateBox.visibility = View.VISIBLE
        fabCreateFolder.visibility = View.VISIBLE
        fabCreateBox.animate().translationY(-resources.getDimension(R.dimen.create_box_fab_position))
        fabCreateFolder.animate().translationY(-resources.getDimension(R.dimen.create_folder_fab_position))
    }

    private fun closeFabMenu() {
        fabMenuShown = false
        fabCreateBox.visibility = View.GONE
        fabExpand.setImageResource(R.drawable.ic_add_entry)
        fabCreateFolder.visibility = View.GONE
        fabCreateBox.animate().translationY(0F)
        fabCreateFolder.animate().translationY(0F)
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

    override fun onItemSelected(position: Int) {
        closeFabMenu()
        showSelectedItemsActionButtons()
        fabExpand.visibility = View.GONE
    }

    override fun onItemUnselected(position: Int) {
        toggleEditSelectedItemButtonVisibility()
        if(dashboardItemAdapter.selectedItems().isEmpty()) {
            hideSelectedItemsActionButtons()
            fabExpand.visibility = View.VISIBLE
        }
    }

    private fun openFolder(item: DashboardItem?) {
        requireActivity().runOnUiThread {
            currentFolder = item
            dashboardItemAdapter.updateList(filterItemsOfCurrentFolder(allItems))
            menu.findItem(R.id.action_delete_folder).isVisible = item != null
            menu.findItem(R.id.action_rename_folder).isVisible = item != null
            dashboardBreadCrumb.updateBreadCrumb(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        val deleteFolderOption = menu.findItem(R.id.action_delete_folder)
        deleteFolderOption.isVisible = currentFolder != null
        val renameFolderOption = menu.findItem(R.id.action_rename_folder)
        renameFolderOption.isVisible = currentFolder != null
        val searchItem = menu.findItem(R.id.dashboard_list_search)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_folder -> {
                deleteFolderOnConfirm()
            }
            R.id.action_rename_folder -> {
                startEditFolderActivity(currentFolder)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteFolderOnConfirm() {
        ConfirmDeleteFolderDialog.showDialog(this)
    }

    private fun startEditFolderActivity(folder: DashboardItem?) {
        val intent = Intent(requireContext(), EditFolderActivity::class.java)
        intent.setCurrentFolder(folder)
        startActivity(intent)
    }

    private fun startEditBoxActivity(box: DashboardItem) {
        val box = AvisioBox(id = box.id, name = box.name!!, parentFolder = box.parentFolder)
        val intent = Intent(requireContext(), EditBoxActivity::class.java)
        intent.setBoxObject(box)
        startActivity(intent)
    }

    fun deleteCurrentFolder() {
        if(currentFolder != null) {
            deleteFolder(currentFolder!!.id)
        }
        openParentFolder()
    }

    private fun deleteFolder(folderId: Long) {
        folderRepository.deleteFolder(AvisioFolder(id = folderId))
    }

    private fun deleteBox(boxId: Long) {
        boxRepository.deleteBox(AvisioBox(id = boxId))
    }

    private fun setOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val searchView = requireView().findViewById<SearchView>(R.id.dashboard_list_search)
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
        if(currentFolder == null) return null
        val parentFolderId = currentFolder!!.parentFolder
        for(item in allItems) {
            if(item.id == parentFolderId && item.type == DashboardItemType.FOLDER) {
                return item
            }
        }
        return null
    }

    private fun setupSelectedItemsActionButtons() {
        editSelectedItemButton = requireView().findViewById(R.id.btn_edit_item)
        editSelectedItemButton.setOnClickListener {
            val selectedItem = dashboardItemAdapter.selectedItems()[0]
            when(selectedItem.type) {
                DashboardItemType.FOLDER -> startEditFolderActivity(selectedItem)
                DashboardItemType.BOX -> startEditBoxActivity(selectedItem)
            }
        }
        deleteSelectedItemsButton = requireView().findViewById(R.id.btn_delete_all)
        deleteSelectedItemsButton.setOnClickListener {
            ConfirmDeleteSelectedItemsDialog.showDialog(this)
        }
        moveSelectedItemsButton = requireView().findViewById(R.id.btn_move_all)
    }

    private fun showSelectedItemsActionButtons() {
        toggleEditSelectedItemButtonVisibility()
        deleteSelectedItemsButton.visibility = View.VISIBLE
        moveSelectedItemsButton.visibility = View.VISIBLE
    }

    private fun hideSelectedItemsActionButtons() {
        editSelectedItemButton.visibility = View.GONE
        deleteSelectedItemsButton.visibility = View.GONE
        moveSelectedItemsButton.visibility = View.GONE
    }

    private fun toggleEditSelectedItemButtonVisibility() {
        if(dashboardItemAdapter.selectedItems().size == 1) {
            editSelectedItemButton.visibility = View.VISIBLE
        } else {
            editSelectedItemButton.visibility = View.GONE
        }
    }

    fun deleteAllSelectedItems() {
        for(item in dashboardItemAdapter.selectedItems()) {
            when(item.type) {
                DashboardItemType.FOLDER -> deleteFolder(item.id)
                DashboardItemType.BOX -> deleteBox(item.id)
            }
        }
        hideSelectedItemsActionButtons()
        fabExpand.visibility = View.VISIBLE
    }

}