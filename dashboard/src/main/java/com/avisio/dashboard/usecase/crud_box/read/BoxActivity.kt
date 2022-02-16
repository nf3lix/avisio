package com.avisio.dashboard.usecase.crud_box.read

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.transfer.*
import com.avisio.dashboard.usecase.crud_card.create.CreateCardActivity
import com.avisio.dashboard.usecase.crud_card.update.EditCardActivity
import com.avisio.dashboard.usecase.crud_box.update.update_box.EditBoxActivity
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.common.workflow.CRUD.*
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_card.read.CardListAdapter
import com.avisio.dashboard.usecase.crud_card.read.CardViewModel
import com.avisio.dashboard.usecase.crud_card.read.CardViewModelFactory
import com.avisio.dashboard.usecase.training.activity.LearnBoxActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoxActivity : AppCompatActivity(), CardListAdapter.CardListOnClickListener {

    companion object {
        const val BOX_DELETE_OBSERVER_REPLY = "BOX_DELETE_REPLY"
    }

    private lateinit var cardViewModel: CardViewModel
    private lateinit var cardListAdapter: CardListAdapter
    private lateinit var box: AvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box)
        box = intent.getBoxObject()!!
        val appBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.box_list_app_bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.title = box.name
        }
        appBar.setOnMenuItemClickListener { item ->
            onMenuItemClicked(item)
        }
    }

    override fun onStart() {
        super.onStart()
        setupView()
        setupFab()
        setupLearnFab()
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
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
                setContentView(R.layout.box_detail_view)
                true
            }
            else -> false
        }
    }

    private fun onEditSelected() {
        val intent = Intent(baseContext, EditBoxActivity::class.java)
        intent.setBoxObject(box)
        startActivity(intent)
        finish()
    }

    private fun confirmDeletion() {
        ConfirmDeleteBoxDialog.showDialog(this, box)
    }

    private fun setupView() {
        setupRecyclerView()
        setupCardViewModel()
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fab_new_card).setOnClickListener {
            startEditCardActivity(CREATE, Card(boxId = box.id))
        }
    }

    private fun setupLearnFab() {
        findViewById<ExtendedFloatingActionButton>(R.id.fab_learn).setOnClickListener {
            val intent = Intent(this, LearnBoxActivity::class.java)
            intent.setBoxObject(box)
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
        cardViewModel = ViewModelProvider(this, CardViewModelFactory(application, box)).get(
            CardViewModel::class.java)
        cardViewModel.getCardListWithSMDetails().observe(this) { cardList ->
            cardListAdapter.submitList(cardList)
        }
    }

    override fun onClick(index: Int) {
        startEditCardActivity(UPDATE, cardListAdapter.currentList[index].card)
    }

    private fun startEditCardActivity(workflow: CRUD, card: Card) {
        val intent = when(workflow) {
            CREATE -> Intent(this, CreateCardActivity::class.java)
            UPDATE ->  Intent(this, EditCardActivity::class.java)
        }
        intent.setCardObject(card)
        startActivity(intent)
    }

}