package com.avisio.dashboard.activity.create_box

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.avisio.dashboard.activity.box_list.BoxListFragment
import com.avisio.dashboard.common.data.model.AvisioBox
import java.util.*

class CreateBoxResultObserver(
    private val boxFragment: BoxListFragment,
    private val registry: ActivityResultRegistry
    ): DefaultLifecycleObserver {

    companion object {
        private const val OBSERVER_REGISTRY_KEY = "CREATE_BOX_RESULT_OBSERVER"
    }

    private lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register(OBSERVER_REGISTRY_KEY, ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            onResult(result)
        }
    }

    private fun onResult(activityResult: ActivityResult) {
        if(activityResult.resultCode == RESULT_OK) {
            val box = AvisioBox(
                name = activityResult.data!!.getStringExtra(CreateBoxActivity.BOX_NAME_REPLY)!!,
                createDate = Date(System.currentTimeMillis())
            )
            boxFragment.newBoxReceived(box)
        }
    }

    fun createBox() {
        content.launch(Intent(boxFragment.context, CreateBoxActivity::class.java))
    }

}