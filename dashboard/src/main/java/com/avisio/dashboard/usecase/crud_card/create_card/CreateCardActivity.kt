package com.avisio.dashboard.usecase.crud_card.create_card

import android.os.Bundle
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentConsumer
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode

class CreateCardActivity : EditCardFragmentConsumer(EditCardFragmentMode.CREATE_CARD, R.id.create_card_fragment_container_view) {

    private lateinit var parcelableCard: ParcelableCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parcelableCard = intent.getParcelableExtra(EditCardFragment.CARD_OBJECT_KEY)!!
        if(savedInstanceState == null) {
            initFragment(parcelableCard)
        }
    }

}