package com.innonorbelis.tomatoia.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innonorbelis.tomatoia.domain.model.DashboardItem
import com.innonorbelis.tomatoia.domain.model.DetalleEnfermedad
import com.innonorbelis.tomatoia.domain.model.Diagnostico
import com.innonorbelis.tomatoia.domain.model.Historial
import com.innonorbelis.tomatoia.domain.useCase.ClasificarImagenUseCase
import com.innonorbelis.tomatoia.domain.useCase.ObtenerDetalleEnfermedadUseCase
import com.innonorbelis.tomatoia.domain.useCase.SaveHistorialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class AnalisisModeloViewModel @Inject constructor(
    private val clasificarImagenUseCase: ClasificarImagenUseCase,
    private val obtenerDetalleEnfermedadUseCase: ObtenerDetalleEnfermedadUseCase,
    private val saveHistorialUseCase: SaveHistorialUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AnalisisModeloUiState>(AnalisisModeloUiState.Idle)
    val state: StateFlow<AnalisisModeloUiState> = _state

    private val _dashboardItems = MutableStateFlow<List<DashboardItem>>(emptyList())
    val dashboardItems: StateFlow<List<DashboardItem>> = _dashboardItems

    private val isProcessing = AtomicBoolean(false)

    fun analizarImagen(bitmap: Bitmap) {
        if (isProcessing.getAndSet(true)) return

        viewModelScope.launch {
            try {
                _state.value = AnalisisModeloUiState.Procesando("Analizando imagen...")

                val result = withContext(Dispatchers.Default) {
                    clasificarImagenUseCase.ejecutarValidado(bitmap)
                }

                _state.value = result
            } catch (e: Exception) {
                _state.value = AnalisisModeloUiState.Error(
                    e.message ?: "No fue posible analizar la imagen."
                )
            } finally {
                isProcessing.set(false)
            }
        }
    }

    fun guardarEnHistorial(
        nombreModelo: String,
        confianza: Int,
        imagenUri: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val detalle = obtenerDetalleEnfermedadUseCase(nombreModelo)
                val nombreMostrar = detalle?.enfermedad?.nombre ?: nombreModelo

                saveHistorialUseCase(
                    Historial(
                        id = 0,
                        nombreModelo = nombreModelo,
                        nombreMostrar = nombreMostrar,
                        confianza = confianza,
                        fecha = System.currentTimeMillis(),
                        imagenUri = imagenUri
                    )
                )
            } catch (e: Exception) {
                // El historial no debe impedir que el usuario vea el resultado de la IA.
                android.util.Log.e("HISTORIAL", "No se pudo guardar el análisis", e)
            }
        }
    }

    fun procesarResultadoIA(nombreModelo: String, confianza: Int, imagenUri: String? = null) {
        viewModelScope.launch {
            val detalle = obtenerDetalleEnfermedadUseCase(nombreModelo)

            if (detalle == null) {
                _dashboardItems.value = listOf(
                    DashboardItem.DiagnosticoItem(
                        Diagnostico(nombreModelo, null, confianza, imagenUri)
                    ),
                    DashboardItem.DescripcionItem(
                        "No se encontró información local para este diagnóstico."
                    )
                )
                return@launch
            }

            construirDashboard(detalle, confianza, imagenUri)
        }
    }

    private fun construirDashboard(
        detalle: DetalleEnfermedad,
        confianza: Int,
        imagenUri: String? = null) {

        val enfermedad = detalle.enfermedad

        val sintomasLista = enfermedad.sintomas
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val recomendacionesLista = enfermedad.recomendaciones
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        _dashboardItems.value = listOf(
            DashboardItem.DiagnosticoItem(
                Diagnostico(enfermedad.nombre, enfermedad.nombreCientifico, confianza, imagenUri)
            ),
            DashboardItem.DescripcionItem(enfermedad.descripcion),
            DashboardItem.SintomasItem(sintomasLista),
            DashboardItem.RecomendacionesItem(recomendacionesLista),
            DashboardItem.TratamientoItem(detalle.tratamientos)
        )
    }

    override fun onCleared() {
        super.onCleared()
        clasificarImagenUseCase.close()
    }
}
