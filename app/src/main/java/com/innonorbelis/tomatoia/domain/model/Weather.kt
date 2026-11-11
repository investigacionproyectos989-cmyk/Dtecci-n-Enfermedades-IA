package com.innonorbelis.tomatoia.domain.model

data class Weather(
       val temperature: Double,
       val description: String,
        val humidity: Double,
        val rainProb: Double,
        val wind: Double,
        val weathercode: Int,
        val time: String,
        val location: String
)
