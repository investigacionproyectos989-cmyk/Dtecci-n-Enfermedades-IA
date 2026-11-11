package com.innonorbelis.tomatoia.data.repository

import com.innonorbelis.tomatoia.data.local.dao.WeatherDao
import com.innonorbelis.tomatoia.data.mapper.toDomain
import com.innonorbelis.tomatoia.data.mapper.toEntity
import com.innonorbelis.tomatoia.domain.model.Weather
import com.innonorbelis.tomatoia.domain.repository.WeatherRepository
import com.innonorbelis.tomatoia.data.remote.api.WeatherApi


import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : WeatherRepository {

    override suspend fun updateWeather(
        lat: Double,
        lon: Double,
        location: String

    ): Weather? {

        val response = api.getWeather(lat, lon)

        val entity = response.toEntity(location)
        dao.clearWeather()
        dao.insertWeather(entity)
        return entity.toDomain()
    }

    override suspend fun getLocalWeather(): Weather? {
        return dao.getWeather()?.toDomain()
    }

    override suspend fun insertWeather(weather: Weather) {
        dao.insertWeather(weather.toEntity())
    }


}