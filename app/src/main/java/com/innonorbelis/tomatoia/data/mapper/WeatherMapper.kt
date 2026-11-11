package com.innonorbelis.tomatoia.data.mapper


import com.innonorbelis.tomatoia.core.util.getDescription
import com.innonorbelis.tomatoia.data.local.entity.WeatherEntity
import com.innonorbelis.tomatoia.domain.model.Weather
import com.innonorbelis.tomatoia.data.remote.dto.WeatherResponse

fun WeatherResponse.toEntity(location: String): WeatherEntity {

    val rainProb =
        hourly.precipitationProbability.firstOrNull() ?: 0.0

    return WeatherEntity(
        id=0,
        temperature = current.temperature2m,
        description = getDescription(current.weatherCode),
        humidity = current.humidity,
        rainProb = rainProb,
        wind = current.windSpeed,
        weathercode = current.weatherCode,
        time = current.time,
        location = location
    )
}
fun WeatherEntity.toDomain(): Weather {
    return Weather(
        temperature,
        description,
        humidity,
        rainProb,
        wind,
        weathercode,
        time,
        location
    )
}
fun Weather.toEntity(): WeatherEntity {
    return WeatherEntity(
        id = 0,
        temperature = temperature,
        description = description,
        humidity = humidity,
        rainProb = rainProb,
        wind = wind,
        weathercode = weathercode,
        time = time,
        location = location
    )
}
