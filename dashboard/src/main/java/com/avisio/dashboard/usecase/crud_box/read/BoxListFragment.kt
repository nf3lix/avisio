package com.avisio.dashboard.usecase.crud_box.read

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.DashboardItemViewModel
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.common.persistence.folder.AvisioFolderRepository
import com.avisio.dashboard.common.ui.breadcrump.BreadCrumb
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.usecase.crud_box.common.ConfirmDeleteSelectedItemsDialog
import com.avisio.dashboard.usecase.crud_box.read.bread_crumb.BreadcrumbListener
import com.avisio.dashboard.usecase.crud_box.read.bread_crumb.DashboardBreadCrumb
import com.avisio.dashboard.usecase.crud_box.read.bread_crumb.DashboardBreadCrumbAdapter
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor.BoxProcessor
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor.DashboardProcessor
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor.FolderProcessor
import com.avisio.dashboard.usecase.crud_box.read.move_items.BoxListView
import com.avisio.dashboard.usecase.crud_box.read.move_items.ConfirmMoveItemsDialog
import com.avisio.dashboard.usecase.crud_box.read.move_items.MoveItemsWorkflow
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.box_list_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BoxListFragment : Fragment(), DashboardItemListAdapter.DashboardItemOnClickListener, BoxListView {

    private lateinit var dashboardItemViewModel: DashboardItemViewModel
    private lateinit var dashboardItemAdapter: DashboardItemListAdapter
    private lateinit var folderRepository: AvisioFolderRepository
    private lateinit var boxRepository: AvisioBoxRepository

    private lateinit var breadCrumb: BreadCrumb
    private lateinit var breadCrumbAdapter: DashboardBreadCrumbAdapter
    private lateinit var dashboardBreadCrumb: DashboardBreadCrumb

    private val moveItemsWorkflow = MoveItemsWorkflow(this)

    private lateinit var boxActivityObserver: BoxActivityResultObserver

    private var currentFolder: DashboardItem? = null
    internal var allItems = listOf<DashboardItem>()
    private var itemsOfCurrentFolder = listOf<DashboardItem>()

    private var fabMenuShown = false
    private lateinit var menu: Menu

    private lateinit var editSelectedItemButton: ExtendedFloatingActionButton
    private lateinit var deleteSelectedItemsButton: ExtendedFloatingActionButton
    private lateinit var moveSelectedItemsButton: ExtendedFloatingActionButton

    private lateinit var fabCreateBox: ExtendedFloatingActionButton
    private lateinit var fabCreateFolder: ExtendedFloatingActionButton
    private lateinit var fabExpand: FloatingActionButton

    var isEditingId: Long = -1

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
        fabCreateBox = requireView().findViewById(R.id.fab_new_box)
        fabCreateFolder = requireView().findViewById(R.id.fab_new_folder)
        fabExpand = requireView().findViewById(R.id.fab_expand)
        folderRepository = AvisioFolderRepository(requireActivity().application)
        boxRepository = AvisioBoxRepository(requireActivity().application)
        (requireActivity() as MainActivity).setSupportActionBar(box_list_app_bar)
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
        dashboardItemRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupBreadCrumb() {
        breadCrumb = requireView().findViewById(R.id.breadCrumb)
        updateItemList()
        breadCrumbAdapter = DashboardBreadCrumbAdapter()
        breadCrumbAdapter.setBreadCrumb(breadCrumb)
        dashboardBreadCrumb = DashboardBreadCrumb(this, breadCrumbAdapter)
        dashboardBreadCrumb.updateBreadCrumb(currentFolder)
        breadCrumb.setOnElementClickListener(BreadcrumbListener(this, dashboardBreadCrumb, moveItemsWorkflow))
    }

    private fun setupBoxViewModel() {
        dashboardItemViewModel = ViewModelProvider(this).get(DashboardItemViewModel::class.java)
        attachDashboardItemAdapter()
    }

    private fun attachDashboardItemAdapter() {
        dashboardItemViewModel.getDashboardItemList().observe(this) { itemList ->
            allItems = itemList
            val filteredList = filterItemsOfCurrentFolder(itemList)
            itemsOfCurrentFolder = filteredList
            dashboardItemAdapter.updateAllItemList(itemList)
            dashboardItemAdapter.updateList(filteredList)
            toggleNoMatchingViewHints(filteredList)
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
            BoxProcessor(currentFolder, this).startCreateChild()
        }

        fabCreateFolder.setOnClickListener {
            FolderProcessor(currentFolder, this).startCreateChild()
        }
    }

    private fun toggleNoMatchingViewHints(filteredList: List<DashboardItem>) {
        Log.d("currentFolder", currentFolder.toString())
        if(filteredList.isEmpty() && currentFolder == null) {
            breadCrumb.visibility = View.GONE
            no_matching_item_label.visibility = View.VISIBLE
        } else {
            breadCrumb.visibility = View.VISIBLE
            no_matching_item_label.visibility = View.GONE
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
        dashboardItemViewModel.deleteBox(boxToDelete!!)
    }

    override suspend fun onClick(index: Int) {
        val clickedItem: DashboardItem?
        try {
            clickedItem = dashboardItemAdapter.currentList[index]
        } catch (ignored: IndexOutOfBoundsException) {
            return
        }
        DashboardProcessor.get(this, clickedItem).openItem()
    }

    override fun onItemSelected(position: Int) {
        if(!moveItemsWorkflow.isActive()) {
            closeFabMenu()
            showSelectedItemsActionButtons()
            fabExpand.visibility = View.GONE
        }
    }

    override fun onItemUnselected(position: Int) {
        toggleEditSelectedItemButtonVisibility()
        requireActivity().runOnUiThread {
            if(dashboardItemAdapter.selectedItems().isEmpty()) {
                hideSelectedItemsActionButtons()
                fabExpand.visibility = View.VISIBLE
                dashboardItemAdapter.moveWorkflowActive = false
                moveItemsWorkflow.finishWorkflow()
            } else {
                if(dashboardHasFolder() || !(singleFolderIsAmongSelectedItems() && currentFolder == null)) {
                    moveSelectedItemsButton.visibility = View.VISIBLE
                } else {
                    moveSelectedItemsButton.visibility = View.GONE
                }
                if(moveItemsWorkflow.isActive()) {
                    updateItemList()
                }
            }
        }
    }

    override fun onMoveItemsToFolderClicked(adapterPosition: Int, item: DashboardItem, selectedItems: List<DashboardItem>) {
        ConfirmMoveItemsDialog.showDialog(this, selectedItems, item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        val renameFolderOption = menu.findItem(R.id.action_rename_folder)
        renameFolderOption.isVisible = currentFolder != null
        val searchItem = menu.findItem(R.id.dashboard_list_search)
        val searchView = SearchView((requireContext() as MainActivity).supportActionBar!!.themedContext)
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        searchItem.actionView = searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterItemList(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterItemList(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun filterItemList(query: String?) {
        val filter = dashboardItemAdapter.getFilter()
        filter.setOnSearchResultListener(object : DashboardItemFilter.OnSearchResultListener {
            override fun onSearchResult(length: Int) {
                toggleNoMatchingItemHint(length)
            }

        })
        filter.filter(query)
    }

    private fun toggleNoMatchingItemHint(filteredListSize: Int) {
        requireActivity().runOnUiThread {
            if(filteredListSize == 0) {
                no_matching_item_label.visibility = View.VISIBLE
                box_list_recycler_view.visibility = View.GONE
            } else {
                no_matching_item_label.visibility = View.GONE
                box_list_recycler_view.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_rename_folder -> {
                isEditingId = currentFolder!!.id
                val folder = currentFolder
                openParentFolder()
                DashboardProcessor.get(this, folder!!).startEditItem()
            }
            R.id.action_stop_workflow -> {
                dashboardItemAdapter.moveWorkflowActive = false
                moveItemsWorkflow.finishWorkflow()
            }
        }
        return super.onOptionsItemSelected(item)
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
        val view = this
        GlobalScope.launch {
            FolderProcessor(parentFolder, view).openItem()
        }
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
            selectedItem.selected = false
            updateItemList()
            hideSelectedItemsActionButtons()
            closeFabMenu()
            fabExpand.visibility = View.VISIBLE
            DashboardProcessor.get(this, selectedItem).startEditItem()
        }
        deleteSelectedItemsButton = requireView().findViewById(R.id.btn_delete_all)
        deleteSelectedItemsButton.setOnClickListener {
            ConfirmDeleteSelectedItemsDialog.showDialog(this)
        }
        moveSelectedItemsButton = requireView().findViewById(R.id.btn_move_all)
        moveSelectedItemsButton.setOnClickListener {
            moveItemsWorkflow.initWorkflow()
            dashboardItemAdapter.moveWorkflowActive = true
        }
    }

    override fun showSelectedItemsActionButtons() {
        toggleEditSelectedItemButtonVisibility()
        deleteSelectedItemsButton.visibility = View.VISIBLE
        if(dashboardHasFolder() || !(singleFolderIsAmongSelectedItems() && currentFolder == null)) {
            moveSelectedItemsButton.visibility = View.VISIBLE
        } else {
            moveSelectedItemsButton.visibility = View.GONE
        }
    }

    override fun hideSelectedItemsActionButtons() {
        requireActivity().runOnUiThread {
            editSelectedItemButton.visibility = View.GONE
            deleteSelectedItemsButton.visibility = View.GONE
            moveSelectedItemsButton.visibility = View.GONE
        }
    }

    private fun toggleEditSelectedItemButtonVisibility() {
        requireActivity().runOnUiThread {
            if(dashboardItemAdapter.selectedItems().size == 1 && !moveItemsWorkflow.isActive()) {
                editSelectedItemButton.visibility = View.VISIBLE
            } else {
                editSelectedItemButton.visibility = View.GONE
            }
        }
    }

    fun deleteAllSelectedItems() {
        for(item in dashboardItemAdapter.selectedItems()) {
            DashboardProcessor.get(this, item).deleteItem()
        }
        hideSelectedItemsActionButtons()
        fabExpand.visibility = View.VISIBLE
    }

    override fun setAppBarTitle(titleId: Int) {
        requireActivity().runOnUiThread {
            (requireActivity() as MainActivity).supportActionBar?.title = requireContext().getString(titleId)
        }
    }

    override fun displayCancelWorkflowMenuItem(displayed: Boolean) {
        requireActivity().runOnUiThread {
            if(this::menu.isInitialized) {
                for(menuItem in moveItemsWorkflow.getDisplayedMenuItems()) {
                    menu.findItem(menuItem.key).isVisible = if(displayed) menuItem.value else !menuItem.value
                }
            }
        }
    }

    override fun displayCancelWorkflowButton(onClick: () -> Unit?) {
        val fab = requireView().findViewById<FloatingActionButton>(R.id.fab_cancel_workflow)
        fab.visibility = View.VISIBLE
        fab.setOnClickListener {
            onClick()
        }
    }

    override fun hideCancelWorkflowButton() {
        val fab = requireView().findViewById<FloatingActionButton>(R.id.fab_cancel_workflow)
        fab.visibility = View.GONE
        fab.setOnClickListener { }
        if(dashboardItemAdapter.selectedItems().isEmpty()) {
            fabExpand.visibility = View.VISIBLE
        } else {
            showSelectedItemsActionButtons()
        }
    }

    override fun updateItemList() {
        dashboardItemAdapter.notifyDataSetChanged()
    }

    override fun setMoveWorkflowActive(active: Boolean) {
        dashboardItemAdapter.moveWorkflowActive = active
    }

    override fun getFolderRepository(): AvisioFolderRepository {
        return folderRepository
    }

    override fun getBoxRepository(): AvisioBoxRepository {
        return boxRepository
    }

    override fun finishMoveWorkflow() {
        moveItemsWorkflow.finishWorkflow()
    }

    override fun context(): Context {
        return requireContext()
    }

    override fun activity(): Activity {
        return requireActivity()
    }

    override fun setCurrentFolder(folder: DashboardItem?) {
        currentFolder = folder
        dashboardItemAdapter.updateList(filterItemsOfCurrentFolder(allItems))
    }

    override fun toggleDeleteMenuItem() {
        menu.findItem(R.id.action_rename_folder).isVisible = currentFolder != null
    }

    override fun searchView(): SearchView {
        return requireView().findViewById(R.id.dashboard_list_search)
    }

    override fun setFilterQuery(query: String) {
        dashboardItemAdapter.getFilter().filter(query)
    }

    override fun boxActivityObserver(): BoxActivityResultObserver {
        return boxActivityObserver
    }

    override fun refreshBreadcrumb() {
        dashboardBreadCrumb.updateBreadCrumb(currentFolder)
    }

    override fun selectedItems(): List<DashboardItem> {
        return dashboardItemAdapter.selectedItems()
    }

    override fun currentFolder(): DashboardItem? {
        return currentFolder
    }

    private fun dashboardHasFolder(): Boolean {
        return amountOfFoldersInDashboard() == 0
    }

    private fun singleFolderIsAmongSelectedItems(): Boolean {
        Log.d("check", "check")
        var count = 0
        for(item in itemsOfCurrentFolder) {
            if(item.type == DashboardItemType.FOLDER) {
                count++
            }
        }
        for(item in itemsOfCurrentFolder) {
            if(item.type == DashboardItemType.FOLDER) {
                if(item !in selectedItems()) {
                    Log.d("false", "false")
                    return false
                }
            }
            // if(item.type == DashboardItemType.FOLDER) {
            //     return item in selectedItems()
            // }
        }
        Log.d("true", "true")
        return true
    }

    private fun amountOfFoldersInDashboard(): Int {
        var count = 0
        for(item in allItems) {
            if(item.type == DashboardItemType.FOLDER) {
                count++
            }
        }
        return count
    }

}