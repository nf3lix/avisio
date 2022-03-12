package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.text.Editable
import android.text.TextWatcher
import com.avisio.dashboard.usecase.crud_card.common.fragment_strategy.CardTypeChangeListener

class CardInputKeyTextWatcher(private val listener: CardTypeChangeListener?, private val flexbox: CardInputFlexBox) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        listener?.onFlexboxInputChanged(flexbox)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }
}