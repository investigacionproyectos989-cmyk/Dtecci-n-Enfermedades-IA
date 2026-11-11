package com.innonorbelis.tomatoia.domain.repository


import com.innonorbelis.tomatoia.domain.model.Weather


interface WeatherRepository {
    suspend fun getLocalWeather(): Weather?
    suspend fun insertWeather(weather: Weather)

    suspend fun updateWeather(
        lat: Double,
        lon: Double,
        location: String): Weather?

}
