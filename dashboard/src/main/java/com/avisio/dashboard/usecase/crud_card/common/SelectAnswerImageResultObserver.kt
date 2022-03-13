package com.avisio.dashboard.usecase.crud_card.common

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class SelectAnswerImageResultObserver(
    private val editCardFragment: EditCardFragment,
    private val registry: ActivityResultRegistry
) : ImageSelectionLifecycleObserver(editCardFragment) {

    companion object {
        private const val OBSERVER_REGISTRY_KEY = "SELECT_ANSWER_IMAGE_OBSERVER"
    }

    private lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register(OBSERVER_REGISTRY_KEY, ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleResult(result)
        }
    }

    override fun startSelectImageActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        content.launch(Intent.createChooser(intent, "Select Image"))
    }

    override fun onBitmapSaved(fileName: String) {
        editCardFragment.answerImageSelected(fileName)
    }

}