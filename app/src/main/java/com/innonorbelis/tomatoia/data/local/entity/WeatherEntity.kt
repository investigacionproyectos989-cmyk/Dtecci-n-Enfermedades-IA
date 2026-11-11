package com.innonorbelis.tomatoia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Creamos la tabla clima para la base de datos
// campos temperatura,humedad,viento,pobabilidadlluvia,fecha,localización
@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey val id: Int = 0,
    val temperature: Double,
    val description: String,
    val humidity: Double,
    val rainProb: Double,
    val wind: Double,
    val weathercode: Int,
    val time: String,
    val location: String
)
