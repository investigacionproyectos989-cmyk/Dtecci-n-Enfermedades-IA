package com.innonorbelis.tomatoia.domain.model

data class Diagnostico(
    val nombre: String,
    val nombreCientifico: String? = null,
    val confianza: Int,
    val imagenUri: String? = null
)
