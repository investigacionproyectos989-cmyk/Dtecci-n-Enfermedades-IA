package com.innonorbelis.tomatoia.domain.useCase

import com.innonorbelis.tomatoia.domain.repository.WeatherRepository
import javax.inject.Inject

class UpdateWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        location: String
    )=repository.updateWeather(lat, lon,location)
}
