package com.avisio.dashboard.activity.box_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class BoxActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_box)
        parcelableBox = intent.getParcelableExtra<ParcelableAvisioBox>("BOX_OBJECT")!!
    }


}