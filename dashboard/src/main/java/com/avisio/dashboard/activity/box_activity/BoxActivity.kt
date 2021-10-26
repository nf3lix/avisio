package com.avisio.dashboard.activity.box_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.crud_card.create_card.CreateCardActivity
import com.avisio.dashboard.activity.crud_card.edit_card.EditCardActivity
import com.avisio.dashboard.activity.edit_box.EditBoxActivity
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxActivity : AppCompatActivity(), CardListAdapter.CardListOnClickListener {

    companion object {
        const val PARCELABLE_BOX_KEY = "BOX_OBJECT"
        const val BOX_DELETE_OBSERVER_REPLY = "BOX_DELETE_REPLY"

        fun startActivity(activity: AppCompatActivity, box: ParcelableAvisioBox) {
            val intent = Intent(activity.baseContext, BoxActivity::class.java)
            intent.putExtra(PARCELABLE_BOX_KEY, box)
            activity.startActivity(intent)
        }

    }

    private lateinit var cardViewModel: CardViewModel
    private lateinit var cardListAdapter: CardListAdapter
    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box)
        parcelableBox = intent.getParcelableExtra(PARCELABLE_BOX_KEY)!!
        supportActionBar?.title = parcelableBox.boxName
    }

    override fun onStart() {
        super.onStart()
        setupView()
        setupFab()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.box_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_box -> {
                onEditSelected()
                true
            }
            R.id.menu_delete_box -> {
                confirmDeletion()
                true
            }
            else -> false
        }
    }

    private fun onEditSelected() {
        val intent = Intent(baseContext, EditBoxActivity::class.java)
        intent.putExtra(PARCELABLE_BOX_KEY, parcelableBox)
        startActivity(intent)
        finish()
    }

    private fun confirmDeletion() {
        val confirmDialog = ConfirmDialog(
            this,
            baseContext.getString(R.string.delete_box_confirm_dialog_title),
            baseContext.getString(R.string.delete_box_confirm_dialog_message)
        )
        confirmDialog.setOnConfirmListener {
            val resultIntent = Intent()
            resultIntent.putExtra(BOX_DELETE_OBSERVER_REPLY, parcelableBox)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        confirmDialog.showDialog()
    }

    private fun setupView() {
        setupRecyclerView()
        setupCardViewModel()
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fab_new_card).setOnClickListener {
            startEditCardActivity(EditCardFragmentMode.CREATE_CARD)
        }
    }

    private fun setupRecyclerView() {
        val cardListRecyclerView = findViewById<RecyclerView>(R.id.card_list_recycler_view)
        cardListAdapter = CardListAdapter(CardListAdapter.CardDifference(), this)
        cardListRecyclerView?.adapter = cardListAdapter
        cardListRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext)
    }

    private fun setupCardViewModel() {
        cardViewModel = ViewModelProvider(this, CardViewModelFactory(application, parcelableBox)).get(CardViewModel::class.java)
        cardViewModel.getCardList().observe(this) { cardList ->
            cardListAdapter.submitList(cardList)
        }
    }

    override fun onClick(index: Int) {
        startEditCardActivity(EditCardFragmentMode.EDIT_CARD)
    }

    private fun startEditCardActivity(fragmentMode: EditCardFragmentMode) {
        val intent = when(fragmentMode) {
            EditCardFragmentMode.CREATE_CARD -> Intent(this, CreateCardActivity::class.java)
            EditCardFragmentMode.EDIT_CARD ->  Intent(this, EditCardActivity::class.java)
        }
        intent.putExtra(EditCardFragment.BOX_OBJECT_KEY, parcelableBox)
        startActivity(intent)
    }

}