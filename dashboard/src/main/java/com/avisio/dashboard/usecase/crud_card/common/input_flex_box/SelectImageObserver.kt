package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.avisio.dashboard.usecase.crud_card.common.ImageSelectionLifecycleObserver

class SelectImageObserver(private val fragment: Fragment,
                          private val launcher: ActivityResultLauncher<String>,
                          private val observer: ImageSelectionLifecycleObserver
) {

    fun onStartSelect() {
        when {
            ContextCompat.checkSelfPermission(fragment.requireContext(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED -> {
                observer.startSelectImageActivity()
            }
            fragment.shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                ActivityCompat.requestPermissions(fragment.requireActivity(), arrayOf(READ_EXTERNAL_STORAGE), PERMISSION_GRANTED)
            }
            else -> {
                launcher.launch(READ_EXTERNAL_STORAGE)
            }
        }
    }

}