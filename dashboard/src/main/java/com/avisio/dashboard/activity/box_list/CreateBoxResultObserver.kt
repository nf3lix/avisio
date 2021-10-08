package com.avisio.dashboard.activity.box_list

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class CreateBoxResultObserver(private val box: BoxListFragment, private val registry: ActivityResultRegistry): DefaultLifecycleObserver {

    private lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register("test", ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            onResult(result)
        }
    }

    private fun onResult(activityResult: ActivityResult) {
        // TODO: handle result
    }

    fun createBox() {
        content.launch(Intent(box.context, CreateBoxActivity::class.java))
    }

}