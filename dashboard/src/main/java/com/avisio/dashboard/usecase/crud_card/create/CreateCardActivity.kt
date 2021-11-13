package com.avisio.dashboard.usecase.crud_card.create

import android.os.Bundle
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.transfer.getCardObject
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.common.workflow.CRUD.*
import com.avisio.dashboard.usecase.crud_card.common.CardFragmentConsumer

class CreateCardActivity : CardFragmentConsumer(CREATE, R.id.create_card_fragment_container_view) {

    private lateinit var card: Card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = intent.getCardObject()!!
        if(savedInstanceState == null) {
            initFragment(card)
        }
    }

}