package com.avisio.dashboard.common.ui.edit_card

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard

abstract class EditCardFragmentConsumer(
    private val fragmentMode: EditCardFragmentMode,
    private val fragmentContainerId: Int)
: AppCompatActivity() {

    fun initFragment(parcelableCard: ParcelableCard) {
        setContentView(fragmentMode.activityLayout)
        supportActionBar?.title = getString(fragmentMode.activityActionBarTitleId)
        val bundle = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to fragmentMode.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to parcelableCard)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentContainerId, EditCardFragment::class.java, bundle)
        transaction.commit()
    }

}