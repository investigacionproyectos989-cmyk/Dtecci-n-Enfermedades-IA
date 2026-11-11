package com.innonorbelis.tomatoia.domain.repository

import com.innonorbelis.tomatoia.domain.model.DetalleEnfermedad

interface EnfermedadRepository {

    suspend fun obtenerDetalleEnfermedad(
        nombreModelo: String
    ): DetalleEnfermedad?
}