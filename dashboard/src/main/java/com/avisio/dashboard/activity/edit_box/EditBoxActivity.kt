package com.avisio.dashboard.activity.edit_box

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_activity.BoxActivity
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class EditBoxActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_box)
        parcelableBox = intent.getParcelableExtra(BoxActivity.PARCELABLE_BOX_KEY)!!
        fillBoxInformation()
    }

    private fun fillBoxInformation() {
        findViewById<EditText>(R.id.box_name_input).setText(parcelableBox.boxName)
    }

}