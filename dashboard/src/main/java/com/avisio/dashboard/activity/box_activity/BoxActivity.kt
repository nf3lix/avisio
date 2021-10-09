package com.avisio.dashboard.activity.box_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class BoxActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parcelableBox = intent.getParcelableExtra("BOX_OBJECT")!!
        supportActionBar?.title = parcelableBox.boxName
    }

}