package com.innonorbelis.tomatoia.domain.model

data class Tratamiento(
    val ingredienteActivo: String,
    val producto: String,
    val dosis: String,
    val pc: Int,
    val pr: Int
)