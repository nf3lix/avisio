package com.avisio.dashboard.activity.crud_card.edit_card

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode

class EditCardActivity : AppCompatActivity() {

    private lateinit var parcelableCard: ParcelableCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.edit_card_action_bar_title)
        setContentView(R.layout.activity_edit_card)
        parcelableCard = intent.getParcelableExtra(EditCardFragment.CARD_OBJECT_KEY)!!
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to EditCardFragmentMode.EDIT_CARD.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to parcelableCard)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.edit_card_fragment_container_view, EditCardFragment::class.java, bundle)
        transaction.commit()
    }

}