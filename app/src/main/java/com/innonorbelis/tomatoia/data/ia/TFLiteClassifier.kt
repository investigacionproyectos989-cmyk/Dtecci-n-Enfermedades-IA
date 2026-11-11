package com.innonorbelis.tomatoia.data.ia

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import javax.inject.Inject

class TFLiteClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val interpreter: Interpreter
    private val labels: List<String>

    init {
        val modelBuffer = loadModelFile(context, "modelo_float16.tflite")
        interpreter = Interpreter(modelBuffer)

        labels = context.assets
            .open("labels.txt")
            .bufferedReader()
            .useLines { lines ->
                lines.map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toList()
            }

        val outputShape = interpreter.getOutputTensor(0).shape()
        val outputSize = outputShape.lastOrNull()
            ?: throw IllegalStateException("No se pudo determinar la salida del modelo TFLite.")

        if (labels.size != outputSize) {
            throw IllegalStateException(
                "Configuración IA incompatible: el modelo devuelve $outputSize clases, " +
                    "pero labels.txt contiene ${labels.size}. Debes usar el labels.txt " +
                    "correspondiente al mismo modelo."
            )
        }
    }

    private fun loadModelFile(context: Context, modelName: String): ByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val fileChannel = inputStream.channel
            return fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                fileDescriptor.startOffset,
                fileDescriptor.declaredLength
            )
        }
    }

    fun predictWithProbabilities(bitmap: Bitmap): Pair<String, FloatArray> {
        val probs = runInference(bitmap)

        val maxIdx = probs.indices.maxByOrNull { probs[it] }
            ?: throw IllegalStateException("El modelo no produjo probabilidades.")

        if (maxIdx !in labels.indices) {
            throw IllegalStateException(
                "El modelo produjo el índice $maxIdx, pero solo existen ${labels.size} etiquetas."
            )
        }

        return labels[maxIdx] to probs
    }

    private fun runInference(bitmap: Bitmap): FloatArray {
        val input = preprocess(bitmap)
        val outputSize = interpreter.getOutputTensor(0).shape().lastOrNull()
            ?: throw IllegalStateException("Salida TFLite inválida.")

        val output = Array(1) { FloatArray(outputSize) }
        interpreter.run(input, output)
        return output[0]
    }
/*DenseNet121 de Keras utiliza un preprocesamiento de ImageNet
 basado en la normalización tipo PyTorch: escala los píxeles a [0, 1],para el  entrenamiento modelo
 */
    private fun preprocess(bitmap: Bitmap): ByteBuffer {
        val inputSize = 512

        val byteBuffer = ByteBuffer.allocateDirect(
            4 * inputSize * inputSize * 3
        ).order(ByteOrder.nativeOrder())

        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap,
            inputSize,
            inputSize,
            true
        )

        val pixels = IntArray(inputSize * inputSize)

        scaledBitmap.getPixels(
            pixels,
            0,
            inputSize,
            0,
            0,
            inputSize,
            inputSize
        )

        var pixelIndex = 0

        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {

                val pixel = pixels[pixelIndex++]

                val r = ((pixel shr 16) and 0xFF) / 255f
                val g = ((pixel shr 8) and 0xFF) / 255f
                val b = (pixel and 0xFF) / 255f

                // Normalización exacta de DenseNet121 de Keras
                val red = (r - 0.485f) / 0.229f
                val green = (g - 0.456f) / 0.224f
                val blue = (b - 0.406f) / 0.225f

                byteBuffer.putFloat(red)
                byteBuffer.putFloat(green)
                byteBuffer.putFloat(blue)
            }
        }

        byteBuffer.rewind()

        return byteBuffer
    }

    fun close() {
        interpreter.close()
    }
}
