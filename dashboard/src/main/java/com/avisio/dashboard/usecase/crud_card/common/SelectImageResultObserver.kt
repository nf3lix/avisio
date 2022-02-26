package com.avisio.dashboard.usecase.crud_card.common

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class SelectImageResultObserver(
    private val editCardFragment: EditCardFragment,
    private val registry: ActivityResultRegistry,
) : DefaultLifecycleObserver {

    companion object {
        private const val OBSERVER_REGISTRY_KEY = "SELECT_IMAGE_OBSERVER"
    }

    private lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register(OBSERVER_REGISTRY_KEY, ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            onResult(result)
        }
    }

    private fun onResult(activityResult: ActivityResult) {
        Log.d("result", activityResult.toString())
    }

    fun startSelectImageActivity() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        content.launch(intent)
    }

}