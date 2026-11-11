package com.innonorbelis.tomatoia.domain.model

data class Enfermedad(

    val id: Int,

    val nombre: String,

    val nombreCientifico: String?,

    val descripcion: String,

    val sintomas: String,

    val recomendaciones: String
)