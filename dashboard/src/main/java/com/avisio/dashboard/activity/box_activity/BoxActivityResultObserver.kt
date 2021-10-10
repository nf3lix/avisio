package com.avisio.dashboard.activity.box_activity

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.avisio.dashboard.activity.box_list.BoxListFragment
import com.avisio.dashboard.common.data.model.AvisioBox
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox

class BoxActivityResultObserver(
    private val boxFragment: BoxListFragment,
    private val registry: ActivityResultRegistry
): DefaultLifecycleObserver {

    companion object {
        private const val OBSERVER_REGISTRY_KEY = "BOX_ACTIVITY_RESULT_OBSERVER"
    }

    private lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register(OBSERVER_REGISTRY_KEY, ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            onResult(result)
        }
    }

    private fun onResult(activityResult: ActivityResult) {
        if(activityResult.resultCode == Activity.RESULT_OK) {
            val boxToDelete = activityResult.data?.getParcelableExtra<ParcelableAvisioBox>(BoxActivity.BOX_DELETE_OBSERVER_REPLY) ?:
                // TODO: error: box could not be deleted
                return
            boxFragment.deleteBox(boxToDelete)
        }
    }

    fun startBoxActivity(avisioBox: AvisioBox) {
        val intent = Intent(boxFragment.context, BoxActivity::class.java)
        intent.putExtra(BoxActivity.PARCELABLE_BOX_KEY, ParcelableAvisioBox.createFromEntity(avisioBox))
        content.launch(intent)
    }

}