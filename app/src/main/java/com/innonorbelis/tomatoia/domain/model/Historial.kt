package com.innonorbelis.tomatoia.domain.model

data class Historial(
    val id: Int,

    val nombreModelo: String,

    val nombreMostrar: String,

    val confianza: Int,

    val fecha: Long,

    val imagenUri: String?
)
