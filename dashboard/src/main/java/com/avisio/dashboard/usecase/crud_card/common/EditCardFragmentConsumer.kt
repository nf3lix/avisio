package com.avisio.dashboard.usecase.crud_card.common

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard

abstract class EditCardFragmentConsumer(
    private val fragmentMode: EditCardFragmentMode,
    private val fragmentContainerId: Int)
: AppCompatActivity() {

    fun initFragment(card: Card) {
        setContentView(fragmentMode.activityLayout)
        supportActionBar?.title = getString(fragmentMode.activityActionBarTitleId)
        val bundle = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to fragmentMode.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to ParcelableCard.createFromEntity(card))
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentContainerId, EditCardFragment::class.java, bundle)
        transaction.commit()
    }

}