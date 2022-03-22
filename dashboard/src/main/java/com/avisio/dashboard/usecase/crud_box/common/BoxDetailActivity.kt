package com.avisio.dashboard.usecase.crud_box.common

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.transfer.getBoxObject
import kotlinx.android.synthetic.main.activity_box_detail.*


class BoxDetailActivity : AppCompatActivity() {

    private lateinit var box: AvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_detail)
        box = intent.getBoxObject()!!
        actionBarSetup()

        box_creation_date_detail_view_content.text = box.createDate.toString() //TODO Format ändern
        box_card_count_detail_view_content.text = "TODO"
        box_card_open_detail_view_content.text = "TODO"
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val actionBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.box_detail_app_bar)
            actionBar?.title = box.name
        }
    }
}