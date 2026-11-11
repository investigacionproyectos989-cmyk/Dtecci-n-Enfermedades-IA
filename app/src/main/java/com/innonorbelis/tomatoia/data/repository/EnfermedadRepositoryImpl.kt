package com.innonorbelis.tomatoia.data.repository

import com.innonorbelis.tomatoia.data.local.dao.EnfermedadDao
import com.innonorbelis.tomatoia.data.mapper.toDomain
import com.innonorbelis.tomatoia.domain.model.DetalleEnfermedad
import com.innonorbelis.tomatoia.domain.repository.EnfermedadRepository

class EnfermedadRepositoryImpl(
    private val dao: EnfermedadDao
) : EnfermedadRepository {

    override suspend fun obtenerDetalleEnfermedad(
        nombreModelo: String
    ): DetalleEnfermedad? {

        val detalle =
            dao.obtenerDetalleCompleto(nombreModelo)
                ?: return null

        return DetalleEnfermedad(

            enfermedad =
                detalle.enfermedad.toDomain(),

            tratamientos =
                detalle.tratamientos.map {
                    it.toDomain()
                }
        )
    }
}