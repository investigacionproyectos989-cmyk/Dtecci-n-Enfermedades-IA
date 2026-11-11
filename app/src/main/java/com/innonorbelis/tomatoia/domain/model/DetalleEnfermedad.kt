package com.innonorbelis.tomatoia.domain.model

data class DetalleEnfermedad(

    val enfermedad: Enfermedad,

    val tratamientos: List<Tratamiento>
)