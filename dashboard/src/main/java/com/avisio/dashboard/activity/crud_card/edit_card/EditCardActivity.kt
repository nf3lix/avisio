package com.avisio.dashboard.activity.crud_card.edit_card

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode

class EditCardActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_card)
        parcelableBox = intent.getParcelableExtra(EditCardFragment.BOX_OBJECT_KEY)!!
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to EditCardFragmentMode.EDIT_CARD,
            EditCardFragment.BOX_OBJECT_KEY to parcelableBox)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.edit_card_fragment_container_view, EditCardFragment::class.java, bundle)
        transaction.commit()
    }

}