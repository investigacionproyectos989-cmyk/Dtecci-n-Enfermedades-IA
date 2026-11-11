package com.innonorbelis.tomatoia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innonorbelis.tomatoia.domain.model.DiseaseRisk
import com.innonorbelis.tomatoia.domain.model.Weather
import com.innonorbelis.tomatoia.domain.useCase.CalculateDiseaseRiskUseCase
import com.innonorbelis.tomatoia.domain.useCase.GetLocalWeatherUseCase
import com.innonorbelis.tomatoia.domain.useCase.SaveWeatherUseCase
import com.innonorbelis.tomatoia.domain.useCase.UpdateWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getLocalWeather: GetLocalWeatherUseCase,
    private val updateWeather: UpdateWeatherUseCase,
    private val saveWeather: SaveWeatherUseCase,
    private val calculateDiseaseRisk: CalculateDiseaseRiskUseCase
) : ViewModel() {

    private val states =
        MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)

    val uiState: StateFlow<WeatherUiState> = states

    fun load(lat: Double, lon: Double) {

        viewModelScope.launch {

            // Primero intentar recuperar el clima guardado localmente.
            val localWeather = try {
                getLocalWeather()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                null
            }

            // Mostrar los datos locales si existen.
            if (localWeather != null) {
                states.value = WeatherUiState.Success(localWeather)
            } else {
                states.value = WeatherUiState.Loading
            }

            // Intentar actualizar los datos desde Internet.
            try {

                val updated = updateWeather(
                    lat,
                    lon,
                    "Toribio,Cauca"
                )

                if (updated != null) {

                    states.value = WeatherUiState.Success(updated)

                } else {

                    // Si no hay actualización, conservar los datos locales.
                    if (localWeather == null) {
                        states.value = WeatherUiState.Error(
                            "Ubicación no disponible"
                        )
                    }
                }

            } catch (e: CancellationException) {

                // No convertir la cancelación de una corrutina en un error.
                throw e

            } catch (e: Exception) {

                // No exponer mensajes técnicos de la API.
                if (localWeather == null) {
                    states.value = WeatherUiState.Error(
                        "Ubicación no disponible"
                    )
                }
            }
        }
    }

    fun save(weather: Weather) {

        viewModelScope.launch {

            try {

                saveWeather(weather)

                val localWeather = getLocalWeather()

                if (localWeather != null) {
                    states.value = WeatherUiState.Success(localWeather)
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Evitar mostrar excepciones técnicas en la interfaz.
            }
        }
    }

    fun getRisk(weather: Weather?): DiseaseRisk {
        return calculateDiseaseRisk(weather)
    }
}