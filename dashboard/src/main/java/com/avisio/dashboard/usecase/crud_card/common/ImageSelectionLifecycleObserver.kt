package com.avisio.dashboard.usecase.crud_card.common

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.DefaultLifecycleObserver

abstract class ImageSelectionLifecycleObserver(private val fragment: EditCardFragment) : DefaultLifecycleObserver {
    abstract fun startSelectImageActivity()

    internal fun getPathFromURI(contentUri: Uri): String? {
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