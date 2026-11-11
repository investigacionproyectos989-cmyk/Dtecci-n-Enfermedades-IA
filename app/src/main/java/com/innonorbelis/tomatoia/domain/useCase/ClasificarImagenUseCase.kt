package com.innonorbelis.tomatoia.domain.useCase

import android.graphics.Bitmap
import android.util.Log
import com.innonorbelis.tomatoia.core.util.ImageValidator
import com.innonorbelis.tomatoia.data.ia.TFLiteClassifier
import com.innonorbelis.tomatoia.ui.viewmodel.AnalisisModeloUiState
import javax.inject.Inject
import kotlin.math.ln

class ClasificarImagenUseCase @Inject constructor(
    private val classifier: TFLiteClassifier
) {
         fun ejecutarValidado(bitmap: Bitmap): AnalisisModeloUiState {

            try {
                // 1. EJECUTAR LA INFERENCIA
                val (label, probs) = classifier.predictWithProbabilities(bitmap)

                // 2. OBTENER LA PROBABILIDAD MÁXIMA
                val maxProb = probs.maxOrNull() ?: 0f

                // 3. ORDENAR PROBABILIDADES
                val sorted = probs.sortedDescending()

                // Diferencia entre las dos clases con mayor probabilidad
                val diffTop2 = if (sorted.size >= 2) {
                    sorted[0] - sorted[1]
                } else {
                    1f
                }

                // 4. CALCULAR ENTROPÍA
                val entropy = -probs.sumOf { p ->
                    if (p > 0f) {
                        p.toDouble() * ln(p.toDouble())
                    } else {
                        0.0
                    }
                }

                // 5. REGISTRAR INFORMACIÓN EN LOGCAT
                Log.d("NEURONAL_DEBUG", "Etiqueta predicha: $label")
                Log.d("NEURONAL_DEBUG", "Probabilidades: ${probs.contentToString()}")
                Log.d("NEURONAL_DEBUG", "MaxProb: $maxProb")
                Log.d("NEURONAL_DEBUG", "Diferencia Top2: $diffTop2")
                Log.d("NEURONAL_DEBUG", "Entropía: $entropy")

                // 6. COMPROBAR QUE LA PREDICCIÓN SEA VÁLIDA
                if (probs.isEmpty() || probs.any { !it.isFinite() }) {
                    Log.e("NEURONAL_DEBUG", "Salida del modelo inválida")

                    return AnalisisModeloUiState.Error(
                        "No fue posible analizar la imagen"
                    )
                }

                // 7. RECHAZAR CLASES EXPLÍCITAS DE IMÁGENES NO VÁLIDAS
                if (
                    label.equals("otros", ignoreCase = true) ||
                    label.equals("no_hoja", ignoreCase = true)
                ) {
                    Log.e("NEURONAL_DEBUG", "RECHAZADA: clase $label")

                    return AnalisisModeloUiState.ImagenNoTomate(
                        "La imagen no corresponde a una hoja de tomate"
                    )
                }

                // 8. EVALUAR CONFIANZA DEL MODELO
                val umbralConfianza = 0.70f

                if (maxProb < umbralConfianza) {
                    Log.w(
                        "NEURONAL_DEBUG",
                        "DIAGNÓSTICO INCIERTO: confianza insuficiente"
                    )

                    return AnalisisModeloUiState.DiagnosticoIncierto(
                        "No se pudo identificar con suficiente confianza. " +
                                "Intenta tomar otra fotografía."
                    )
                }

                // 9. EVALUAR DIFERENCIA ENTRE LAS DOS CLASES PRINCIPALES
                if (diffTop2 < 0.15f) {
                    Log.w(
                        "NEURONAL_DEBUG",
                        "DIAGNÓSTICO INCIERTO: probabilidades muy cercanas"
                    )

                    return AnalisisModeloUiState.DiagnosticoIncierto(
                        "El modelo no logra diferenciar claramente el resultado. " +
                                "Intenta tomar otra fotografía."
                    )
                }

                // 10. MOSTRAR RESULTADO
                Log.d("NEURONAL_DEBUG", "ANÁLISIS EXITOSO: $label")

                return AnalisisModeloUiState.Exito(
                    label,
                    (maxProb * 100).toInt()
                )

            } catch (e: Exception) {

                Log.e(
                    "NEURONAL_DEBUG",
                    "Error durante la clasificación: ${e.message}",
                    e
                )

                return AnalisisModeloUiState.Error(
                    "Ocurrió un error al analizar la imagen"
                )
            }
        }

        fun close() {
            classifier.close()
        }
    }
