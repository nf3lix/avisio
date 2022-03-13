package com.avisio.dashboard.usecase.crud_card.common

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultRegistry

class SelectAnswerImageResultObserver(
    private val editCardFragment: EditCardFragment,
    registry: ActivityResultRegistry
) : ImageSelectionLifecycleObserver(editCardFragment, registry, observerRegistryKey = "SELECT_ANSWER_IMAGE_OBSERVER") {

    override fun startSelectImageActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        content.launch(Intent.createChooser(intent, "Select Image"))
    }

    override fun onBitmapSaved(fileName: String) {
        editCardFragment.answerImageSelected(fileName)
    }

}