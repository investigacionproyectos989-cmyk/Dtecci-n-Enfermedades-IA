package com.innonorbelis.tomatoia.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innonorbelis.tomatoia.domain.model.Historial
import com.innonorbelis.tomatoia.domain.useCase.DeleteHistorialUseCase
import com.innonorbelis.tomatoia.domain.useCase.FiltrarHistorialUseCase
import com.innonorbelis.tomatoia.domain.useCase.GetHistorialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val getHistorialUseCase: GetHistorialUseCase,
    private val filtrarHistorialUseCase: FiltrarHistorialUseCase,
    private val deleteHistorialUseCase: DeleteHistorialUseCase
) : ViewModel() {

    private val _filtroSeleccionado = MutableStateFlow("Todas")

    val filtroSeleccionado: StateFlow<String> = _filtroSeleccionado

    val historial: StateFlow<List<Historial>> =
        _filtroSeleccionado
            .flatMapLatest { filtro ->
                val flujo = if (filtro == "Todas") {
                    getHistorialUseCase()
                } else {
                    filtrarHistorialUseCase(obtenerNombreModelo(filtro))
                }

                // Capturar errores por consulta, para que el flujo de filtros
                // pueda seguir funcionando después de un fallo puntual.
                flujo.catch { error ->
                    Log.e(TAG, "Error al consultar el historial con filtro '$filtro'", error)
                    emit(emptyList())
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun seleccionarFiltro(filtro: String) {
        _filtroSeleccionado.value = filtro
    }

    fun eliminarHistorial(id: Int) {
        viewModelScope.launch {
            try {
                deleteHistorialUseCase(id)
            } catch (error: Exception) {
                Log.e(TAG, "Error al eliminar el registro $id del historial", error)
            }
        }
    }

    private fun obtenerNombreModelo(nombre: String): String {
        return when (nombre) {
            "Mancha bacteriana" -> "Bacterial_spot"
            "Tizón temprano" -> "Early_blight"
            "Tizón tardío (Gota)" -> "Late_blight"
            "Moho de la hoja" -> "Leaf_Mold"
            "Septoria" -> "Septoria_leaf_spot"
            "Mancha objetivo" -> "Target_Spot"
            "Mildéu polvoroso" -> "powdery_mildew"
            else -> ""
        }
    }

    companion object {
        private const val TAG = "HistorialViewModel"
    }
}
