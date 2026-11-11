package com.innonorbelis.tomatoia.core.util

import android.graphics.Bitmap

object ImageValidator {

    fun esHoja(bitmap: Bitmap): Boolean {
        var greenCount = 0
        val total = bitmap.width * bitmap.height

        for (y in 0 until bitmap.height step 10) {
            for (x in 0 until bitmap.width step 10) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF

                if (g > r && g > b) {
                    greenCount++
                }
            }
        }

        val ratio = greenCount.toFloat() / (total / 100)
        return ratio > 0.3f
    }
}