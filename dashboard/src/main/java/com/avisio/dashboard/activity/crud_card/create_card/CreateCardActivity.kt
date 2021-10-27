package com.avisio.dashboard.activity.crud_card.create_card

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode

class CreateCardActivity : AppCompatActivity() {

    private lateinit var parcelableCard: ParcelableCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        parcelableCard = intent.getParcelableExtra(EditCardFragment.CARD_OBJECT_KEY)!!
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to EditCardFragmentMode.CREATE_CARD.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to parcelableCard)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.create_card_fragment_container_view, EditCardFragment::class.java, bundle)
        transaction.commit()
    }

}