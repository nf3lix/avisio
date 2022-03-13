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

abstract class ImageSelectionLifecycleObserver(
    private val fragment: EditCardFragment,
    private val registry: ActivityResultRegistry,
    private val observerRegistryKey: String
) : DefaultLifecycleObserver {

    internal lateinit var content: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        content = registry.register(observerRegistryKey, ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleResult(result)
        }
    }

    private fun handleResult(result: ActivityResult) {
        if(result.data?.data == null) {
            return
        }
        val path = getPathFromURI(result.data!!.data!!) ?: return
        val file = File(path)
        val selectedImageUri = Uri.fromFile(file)
        val drawable = Drawable.createFromPath(selectedImageUri.path!!)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val fileName = UUID.randomUUID().toString()
        val imageStorage = CardImageStorage(fragment.requireContext())
        imageStorage.saveBitmap(bitmap, fileName)
        onBitmapSaved(fileName)
    }

    abstract fun onBitmapSaved(fileName: String)
    abstract fun startSelectImageActivity()

    private fun getPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = fragment.requireActivity().contentResolver.query(contentUri, proj, null, null, null)!!
        if (cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(columnIndex)
        }
        cursor.close()
        return res
    }

}