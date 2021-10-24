package com.avisio.dashboard.activity.create_card

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode

class CreateCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val bundle = bundleOf(EditCardFragment.FRAGMENT_MODE_KEY to EditCardFragmentMode.CREATE_CARD)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.create_card_fragment_container_view, EditCardFragment::class.java, bundle)
        transaction.commit()
    }

}