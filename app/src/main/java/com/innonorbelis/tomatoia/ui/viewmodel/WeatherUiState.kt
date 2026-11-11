package com.innonorbelis.tomatoia.ui.viewmodel

import com.innonorbelis.tomatoia.domain.model.Weather

//Estado de clima  de clases
sealed class WeatherUiState {
    object  Loading : WeatherUiState()
    data class  Success (val weather: Weather?): WeatherUiState()
    data class Error(val  message: String): WeatherUiState()

}