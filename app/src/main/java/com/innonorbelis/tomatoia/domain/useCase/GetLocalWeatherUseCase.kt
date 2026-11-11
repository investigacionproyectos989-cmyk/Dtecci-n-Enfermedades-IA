package com.innonorbelis.tomatoia.domain.useCase

import com.innonorbelis.tomatoia.domain.model.Weather
import com.innonorbelis.tomatoia.domain.repository.WeatherRepository
import javax.inject.Inject

class GetLocalWeatherUseCase @Inject constructor(
    private val repo: WeatherRepository) {
    suspend operator fun invoke(): Weather? =repo.getLocalWeather()
    }

