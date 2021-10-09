package com.avisio.dashboard.activity.box_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class BoxActivity : AppCompatActivity() {

    companion object {
        const val PARCELABLE_BOX_KEY = "BOX_OBJECT"
        const val BOX_DELETE_OBSERVER_REPLY = "BOX_DELETE_REPLY"
    }

    private lateinit var parcelableBox: ParcelableAvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parcelableBox = intent.getParcelableExtra(PARCELABLE_BOX_KEY)!!
        supportActionBar?.title = parcelableBox.boxName
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.box_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_box -> {
                true
            }
            R.id.menu_delete_box -> {
                onDeleteSelected()
                true
            }
            else -> false
        }
    }

    private fun onEditSelected() {

    }

    private fun onDeleteSelected() {
        val resultIntent = Intent()
        resultIntent.putExtra(BOX_DELETE_OBSERVER_REPLY, parcelableBox)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

}