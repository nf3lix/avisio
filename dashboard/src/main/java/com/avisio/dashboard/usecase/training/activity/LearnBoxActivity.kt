package com.avisio.dashboard.usecase.training.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox

class LearnBoxActivity : AppCompatActivity() {

    companion object {
        const val BOX_OBJECT_KEY = "BOX_OBJECT_KEY"
    }

    private lateinit var box: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_box)
        supportActionBar?.title = getString(R.string.learn_activity_title)
        box = intent.getParcelableExtra(BOX_OBJECT_KEY)!!
        initFragment()
    }

    private fun initFragment() {
        val bundle = bundleOf()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.learn_card_fragment_container_view, LearnBoxFragment::class.java, bundle)
        transaction.commit()
    }

}