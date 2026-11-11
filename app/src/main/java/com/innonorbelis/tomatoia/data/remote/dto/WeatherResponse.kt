package com.innonorbelis.tomatoia.data.remote.dto

import com.google.gson.annotations.SerializedName

// creo la data clase  RespuestaMeteorologicas
data class WeatherResponse(
    val current:CurrentDto,
    val hourly:HourlyDto
)

data class CurrentDto(

    @SerializedName("temperature_2m")
    val temperature2m: Double,

    @SerializedName("relative_humidity_2m")
    val humidity: Double,

    @SerializedName("wind_speed_10m")
    val windSpeed: Double,

    @SerializedName("weather_code")
    val weatherCode: Int,

    val time: String
)

data class HourlyDto(

    val time: List<String>,

    @SerializedName("temperature_2m")
    val temperature: List<Double>,

    @SerializedName("relative_humidity_2m")
    val humidity: List<Double>,

    @SerializedName("wind_speed_10m")
    val windSpeed: List<Double>,

    @SerializedName("precipitation_probability")
    val precipitationProbability: List<Double>
)
