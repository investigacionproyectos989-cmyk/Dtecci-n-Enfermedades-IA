package com.innonorbelis.tomatoia.data.ia


import android.graphics.Bitmap

object ImagePreprocessor {

    fun preprocess(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val size = 512
        val resized = Bitmap.createScaledBitmap(
            bitmap,
            size,
            size,
            true)

        val input = Array(1) {
            Array(size) {
                Array(size) {
                    FloatArray(3)
                }
            }
        }

        for (y in 0 until size) {
            for (x in 0 until size) {
                val pixel = resized.getPixel(x, y)

                val r = (pixel shr 16 and 0xFF) / 255.0f
                val g = (pixel shr 8 and 0xFF) / 255.0f
                val b = (pixel and 0xFF) / 255.0f

                //  DenseNet121 espera valores en [-1, 1]
                input[0][y][x][0] = (r * 2f) - 1f
                input[0][y][x][1] = (g * 2f) - 1f
                input[0][y][x][2] = (b * 2f) - 1f
            }
        }

        return input
    }
}
