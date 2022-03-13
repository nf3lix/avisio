package com.avisio.dashboard.usecase.crud_card.common

import androidx.activity.result.ActivityResultRegistry

class SelectAnswerImageResultObserver(
    private val editCardFragment: EditCardFragment,
    registry: ActivityResultRegistry
) : ImageSelectionLifecycleObserver(editCardFragment, registry, observerRegistryKey = "SELECT_ANSWER_IMAGE_OBSERVER") {

    override fun onBitmapSaved(fileName: String) {
        editCardFragment.answerImageSelected(fileName)
    }

}