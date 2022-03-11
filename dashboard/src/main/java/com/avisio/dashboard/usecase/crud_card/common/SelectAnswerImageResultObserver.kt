package com.avisio.dashboard.usecase.crud_card.common

import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.avisio.dashboard.common.persistence.card.CardImageStorage
import java.io.File
import java.util.*

class SelectAnswerImageResultObserver(
    private val editCardFragment: EditCardFragment,
    private val registry: ActivityResultRegistry
) : DefaultLifecycleObserver {

    companion object {
        private const val OBSERVER_REGISTRY_KEY = "SELECT_ANSWER_IMAGE_OBSERVER"
    }

    private lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register(OBSERVER_REGISTRY_KEY, ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.data?.data == null) {
                return@register
            }
            val path = getPathFromURI(result.data!!.data!!) ?: return@register
            val file = File(path)
            val selectedImageUri = Uri.fromFile(file)
            val drawable = Drawable.createFromPath(selectedImageUri.path!!)
            val bitmap = (drawable as BitmapDrawable).bitmap
            val fileName = UUID.randomUUID().toString()
            val imageStorage = CardImageStorage(editCardFragment.requireContext())
            imageStorage.saveBitmap(bitmap, fileName)
            editCardFragment.answerImageSelected(fileName)
        }
    }

    private fun getPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = editCardFragment.requireActivity().contentResolver.query(contentUri, proj, null, null, null)!!
        if (cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(columnIndex)
        }
        cursor.close()
        return res
    }

    fun startSelectImageActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        content.launch(Intent.createChooser(intent, "Select Image"))
    }

}