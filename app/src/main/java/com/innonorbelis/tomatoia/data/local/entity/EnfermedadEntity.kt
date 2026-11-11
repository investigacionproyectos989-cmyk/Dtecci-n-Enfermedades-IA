package com.innonorbelis.tomatoia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enfermedad")
data class EnfermedadEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombreModelo: String,     // IA
    val nombreMostrar: String,    // UI
    val nombreCientifico: String?,
    val descripcion: String,
    val sintomas: String,              // separados por \n
    val recomendacionInmediata: String
)