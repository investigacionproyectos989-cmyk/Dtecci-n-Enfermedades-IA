package com.innonorbelis.tomatoia.ui.viewmodel

sealed class AnalisisModeloUiState {
    object Idle : AnalisisModeloUiState()
    data class Procesando(val mensaje: String) : AnalisisModeloUiState()

    // Nuevo estado para diferenciar incertidumbre
    data class DiagnosticoIncierto(val mensaje: String) : AnalisisModeloUiState()

    /** Resultado de rechazo: no es una enfermedad de hoja de tomate. */
    data class ImagenNoTomate(val mensaje: String) : AnalisisModeloUiState()

    data class Exito(val label: String, val confianza: Int) : AnalisisModeloUiState()
    data class Error(val mensaje: String) : AnalisisModeloUiState()
}
