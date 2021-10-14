package com.avisio.dashboard.activity.edit_box

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_activity.BoxActivity
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox
import com.avisio.dashboard.common.persistence.AvisioBoxRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditBoxActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox
    private lateinit var boxDao: AvisioBoxRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_box)
        setSupportActionBar(findViewById(R.id.toolbar))
        boxDao = AvisioBoxRepository(this.application)
        parcelableBox = intent.getParcelableExtra(BoxActivity.PARCELABLE_BOX_KEY)!!
        fillBoxInformation()
        setupFab()
    }

    private fun fillBoxInformation() {
        findViewById<EditText>(R.id.box_name_input).setText(parcelableBox.boxName)
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fab_edit_box).setOnClickListener {
            val updatedBox = getUpdatedBox()
            if(boxChanged(updatedBox)) {
                boxDao.updateBox(getUpdatedBox())
            }
            startBoxActivity()
        }
    }

    private fun getUpdatedBox(): ParcelableAvisioBox {
        val updatedName = findViewById<EditText>(R.id.box_name_input).text.toString()
        return ParcelableAvisioBox(parcelableBox.boxId, updatedName)
    }

    private fun boxChanged(updatedBox: ParcelableAvisioBox): Boolean {
        return parcelableBox.boxName != updatedBox.boxName
    }

    private fun startBoxActivity() {
        BoxActivity.startActivity(this, getUpdatedBox())
        finish()
    }

}