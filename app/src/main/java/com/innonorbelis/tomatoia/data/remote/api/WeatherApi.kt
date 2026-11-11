package com.innonorbelis.tomatoia.data.remote.api

import com.innonorbelis.tomatoia.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getWeather(

        @Query("latitude")
        lat: Double,

        @Query("longitude")
        lon: Double,

        @Query("current")
        current: String =
            "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code",

        @Query("hourly")
        hourly: String =
            "temperature_2m,relative_humidity_2m,wind_speed_10m,precipitation_probability",

        @Query("timezone")
        timezone: String = "America/Bogota"

    ): WeatherResponse
}