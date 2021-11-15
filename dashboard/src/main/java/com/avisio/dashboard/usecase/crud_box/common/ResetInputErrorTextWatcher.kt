package com.avisio.dashboard.usecase.crud_box.common

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

class ResetInputErrorTextWatcher(private val boxNameInput: TextInputLayout) : TextWatcher {

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        boxNameInput.isErrorEnabled = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun afterTextChanged(s: Editable?) { }

}