package com.avisio.dashboard.usecase.crud_card.common

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import com.avisio.dashboard.common.persistence.card.CardImageStorage
import java.io.File
import java.util.*


class SelectQuestionImageResultObserver(
    private val editCardFragment: EditCardFragment,
    registry: ActivityResultRegistry,
) : ImageSelectionLifecycleObserver(editCardFragment, registry, observerRegistryKey = "SELECT_IMAGE_OBSERVER") {

    override fun startSelectImageActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        content.launch(Intent.createChooser(intent, "Select Image"))
    }

    override fun onBitmapSaved(fileName: String) {
        editCardFragment.questionImageSelected(fileName)
    }

}