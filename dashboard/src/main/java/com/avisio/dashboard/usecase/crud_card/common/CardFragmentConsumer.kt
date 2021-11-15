package com.avisio.dashboard.usecase.crud_card.common

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.common.workflow.CRUD.*

abstract class CardFragmentConsumer(
    private val workflow: CRUD,
    private val fragmentContainerId: Int)
: AppCompatActivity() {

    fun initFragment(card: Card) {
        setContentView(getActivityLayout())
        supportActionBar?.title = getActionBarTitle()
        val bundle = bundleOf(
            EditCardFragment.CARD_CRUD_WORKFLOW to workflow.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to ParcelableCard.createFromEntity(card))
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentContainerId, EditCardFragment::class.java, bundle)
        transaction.commit()
    }

    private fun getActivityLayout(): Int = when(workflow) {
        CREATE -> R.layout.activity_create_card
        UPDATE -> R.layout.activity_edit_card
    }

    private fun getActionBarTitle(): String = when(workflow) {
        CREATE -> getString(R.string.create_card_action_bar_title)
        UPDATE -> getString(R.string.edit_card_action_bar_title)
    }

}