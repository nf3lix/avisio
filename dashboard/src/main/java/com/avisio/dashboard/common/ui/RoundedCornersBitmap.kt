package com.avisio.dashboard.common.ui

import android.graphics.*

class RoundedCornersBitmap {

    companion object {

        fun withRoundedCorners(bitmap: Bitmap, radius: Int): Bitmap {
            val roundedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(roundedBitmap)
            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            canvas.drawRoundRect((RectF(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat())), radius.toFloat(), radius.toFloat(), paint)
            return roundedBitmap
        }

    }

}