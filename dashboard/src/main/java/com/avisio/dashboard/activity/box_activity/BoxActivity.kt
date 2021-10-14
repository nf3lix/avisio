package com.avisio.dashboard.activity.box_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.edit_box.EditBoxActivity
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox
import com.avisio.dashboard.common.ui.ConfirmDialog

class BoxActivity : AppCompatActivity(), ConfirmDialog.ConfirmDialogListener {

    companion object {
        const val PARCELABLE_BOX_KEY = "BOX_OBJECT"
        const val BOX_DELETE_OBSERVER_REPLY = "BOX_DELETE_REPLY"

        fun startActivity(activity: AppCompatActivity, box: ParcelableAvisioBox) {
            val intent = Intent(activity.baseContext, BoxActivity::class.java)
            intent.putExtra(PARCELABLE_BOX_KEY, box)
            activity.startActivity(intent)
        }

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
                onEditSelected()
                true
            }
            R.id.menu_delete_box -> {
                confirmDeletion()
                true
            }
            else -> false
        }
    }

    private fun onEditSelected() {
        val intent = Intent(baseContext, EditBoxActivity::class.java)
        intent.putExtra(PARCELABLE_BOX_KEY, parcelableBox)
        startActivity(intent)
        finish()
    }

    private fun confirmDeletion() {
        ConfirmDialog<ParcelableAvisioBox>(
            this,
            this,
            baseContext.getString(R.string.delete_box_confirm_dialog_title),
            baseContext.getString(R.string.delete_box_confirm_dialog_message))
        .showDialog(parcelableBox)
    }

    override fun onConfirm(data: Any) {
        val resultIntent = Intent()
        resultIntent.putExtra(BOX_DELETE_OBSERVER_REPLY, parcelableBox)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

}