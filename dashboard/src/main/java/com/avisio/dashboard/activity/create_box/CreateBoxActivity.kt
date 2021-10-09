package com.avisio.dashboard.activity.create_box

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.avisio.dashboard.R

class CreateBoxActivity : AppCompatActivity() {

    companion object {
        const val BOX_NAME_REPLY: String = "BOX_NAME_REPLY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_box)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupFab()
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fab_new_box).setOnClickListener {
            handleFabClicked()
        }
    }

    private fun handleFabClicked() {
        val boxNameInput = findViewById<EditText>(R.id.box_name_input).text
        when(TextUtils.isEmpty(boxNameInput)) {
            true ->
                handleInvalidInput()
            false ->
                handleValidInput(boxNameInput.toString())
        }
    }

    private fun handleInvalidInput() {
        Toast.makeText(this, "Please specify a name", Toast.LENGTH_LONG).show() // TODO: Replace text by config value
    }

    private fun handleValidInput(boxNameInput: String) {
        val resultIntent = Intent()
        resultIntent.putExtra(BOX_NAME_REPLY, boxNameInput)
        setResult(RESULT_OK, resultIntent)
        finish()
    }


}