package com.innonorbelis.tomatoia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial")
data class HistorialEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombreModelo: String,

    val nombreMostrar: String,

    val confianza: Int,

    val fecha: Long,

    val imagenUri: String?
)