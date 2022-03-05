package com.avisio.dashboard.common.persistence.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class CardImageStorage(private val context: Context) {

    fun saveBitmap(bitmap: Bitmap, fileName: String) {
        val bitmapBytes = getBytes(bitmap)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(bitmapBytes)
        }
    }

    fun loadBitmap(fileName: String): Bitmap {
        val byteArray = context.openFileInput(fileName).readBytes()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun getBytes(bitmap: Bitmap): ByteArray {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        return output.toByteArray()
    }

}