package com.avisio.dashboard.activity.box_activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class BoxActivity : AppCompatActivity() {

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parcelableBox = intent.getParcelableExtra("BOX_OBJECT")!!
        supportActionBar?.title = parcelableBox.boxName
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.box_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id. -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}