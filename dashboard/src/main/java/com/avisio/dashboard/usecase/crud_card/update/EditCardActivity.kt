package com.avisio.dashboard.usecase.crud_card.update

import android.os.Bundle
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragmentConsumer
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragmentMode

class EditCardActivity : EditCardFragmentConsumer(EditCardFragmentMode.EDIT_CARD, R.id.edit_card_fragment_container_view) {

    private lateinit var parcelableCard: ParcelableCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parcelableCard = intent.getParcelableExtra(EditCardFragment.CARD_OBJECT_KEY)!!
        if(savedInstanceState == null) {
            initFragment(parcelableCard)
        }
    }

}