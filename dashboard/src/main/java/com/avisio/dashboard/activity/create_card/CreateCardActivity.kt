package com.avisio.dashboard.activity.create_card

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment

class CreateCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)
        if(savedInstanceState == null) {
            initFragment()
        }
    }

    private fun initFragment() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.create_card_fragment_container_view, EditCardFragment())
        transaction.commit()
    }

}