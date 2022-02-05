package com.avisio.dashboard.usecase.crud_box.read

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.setBoxObject
import com.avisio.dashboard.common.persistence.box.AvisioBoxRepository
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BoxActivityResultObserver(
    private val boxFragment: BoxListFragment,
    private val registry: ActivityResultRegistry,
    application: Application
): DefaultLifecycleObserver {

    companion object {
        private const val OBSERVER_REGISTRY_KEY = "BOX_ACTIVITY_RESULT_OBSERVER"
    }

    private lateinit var content: ActivityResultLauncher<Intent>
    private val boxRepository: AvisioBoxRepository = AvisioBoxRepository(application)

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register(OBSERVER_REGISTRY_KEY, ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            onResult(result)
        }
    }

    private fun onResult(activityResult: ActivityResult) {
        if(activityResult.resultCode == Activity.RESULT_OK) {
            val boxToDelete = activityResult.data?.getParcelableExtra<ParcelableAvisioBox>(
                BoxActivity.BOX_DELETE_OBSERVER_REPLY
            )!!
            boxFragment.deleteBox(boxToDelete)
        }
    }

    suspend fun startBoxActivity(item: DashboardItem) {
        val box: AvisioBox?
        withContext(Dispatchers.Default) {
            box = boxRepository.getBoxById(item.id)
        }
        if(box != null) {
            val intent = Intent(boxFragment.context, BoxActivity::class.java)
            intent.setBoxObject(box)
            content.launch(intent)
            return
        }
        Toast.makeText(boxFragment.context, "Could not load box", Toast.LENGTH_LONG).show()
    }

}