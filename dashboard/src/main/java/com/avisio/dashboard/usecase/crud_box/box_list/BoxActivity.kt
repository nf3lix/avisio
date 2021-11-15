package com.avisio.dashboard.usecase.crud_box.box_list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.ui.ConfirmDialog
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode
import com.avisio.dashboard.usecase.crud_box.edit_box.EditBoxActivity
import com.avisio.dashboard.usecase.crud_card.card_list.CardListAdapter
import com.avisio.dashboard.usecase.crud_card.card_list.CardViewModel
import com.avisio.dashboard.usecase.crud_card.card_list.CardViewModelFactory
import com.avisio.dashboard.usecase.crud_card.create_card.CreateCardActivity
import com.avisio.dashboard.usecase.crud_card.edit_card.EditCardActivity
import com.avisio.dashboard.usecase.training.activity.LearnBoxActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxActivity : AppCompatActivity(), CardListAdapter.CardListOnClickListener {

    companion object {
        const val PARCELABLE_BOX_KEY = "BOX_OBJECT"
        const val BOX_DELETE_OBSERVER_REPLY = "BOX_DELETE_REPLY"
    }

    private lateinit var cardViewModel: CardViewModel
    private lateinit var cardListAdapter: CardListAdapter
    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box)
        parcelableBox = intent.getParcelableExtra(PARCELABLE_BOX_KEY)!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.box_list_app_bar).title = parcelableBox.boxName
        }
        // supportActionBar?.title =
    }

    override fun onStart() {
        super.onStart()
        setupView()
        setupFab()
        setupLearnFab()
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
            R.id.menu_information_box -> {
                //information()
                Toast.makeText(applicationContext, "click", Toast.LENGTH_SHORT).show()
                setContentView(R.layout.box_detail_view)
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
            startEditCardActivity(EditCardFragmentMode.CREATE_CARD, Card(boxId = parcelableBox.boxId))
        }
    }

    private fun setupLearnFab() {
        findViewById<ExtendedFloatingActionButton>(R.id.fab_learn).setOnClickListener {
            val intent = Intent(this, LearnBoxActivity::class.java)
            intent.putExtra(LearnBoxActivity.BOX_OBJECT_KEY, parcelableBox)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val cardListRecyclerView = findViewById<RecyclerView>(R.id.card_list_recycler_view)
        cardListAdapter = CardListAdapter(CardListAdapter.CardDifference(), this)
        cardListRecyclerView?.adapter = cardListAdapter
        cardListRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext)
    }

    private fun setupCardViewModel() {
        cardViewModel = ViewModelProvider(this, CardViewModelFactory(application, parcelableBox)).get(
            CardViewModel::class.java)
        cardViewModel.getCardList().observe(this) { cardList ->
            cardListAdapter.submitList(cardList)
        }
    }

    override fun onClick(index: Int) {
        startEditCardActivity(EditCardFragmentMode.EDIT_CARD, cardListAdapter.currentList[index])
    }

    private fun startEditCardActivity(fragmentMode: EditCardFragmentMode, card: Card) {
        val intent = when(fragmentMode) {
            EditCardFragmentMode.CREATE_CARD -> Intent(this, CreateCardActivity::class.java)
            EditCardFragmentMode.EDIT_CARD ->  Intent(this, EditCardActivity::class.java)
        }
        intent.putExtra(EditCardFragment.CARD_OBJECT_KEY, ParcelableCard.createFromEntity(card))
        startActivity(intent)
    }

}