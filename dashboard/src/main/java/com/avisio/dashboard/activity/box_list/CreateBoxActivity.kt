package com.avisio.dashboard.activity.box_list

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R

class CreateBoxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_box)
        setSupportActionBar(findViewById(R.id.toolbar))

        val boxNameInput = findViewById<EditText>(R.id.box_name_input)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val resultIntent = Intent()
            if(TextUtils.isEmpty(boxNameInput.text.toString())) {

            } else {
                resultIntent.putExtra("CREATE_REPLY", boxNameInput.text.toString())
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}