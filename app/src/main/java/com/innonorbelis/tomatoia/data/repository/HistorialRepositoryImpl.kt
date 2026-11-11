package com.innonorbelis.tomatoia.data.repository

import com.innonorbelis.tomatoia.data.local.dao.HistorialDao
import com.innonorbelis.tomatoia.data.local.entity.HistorialEntity
import com.innonorbelis.tomatoia.data.mapper.toDomain
import com.innonorbelis.tomatoia.domain.model.Historial
import com.innonorbelis.tomatoia.domain.repository.HistorialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistorialRepositoryImpl @Inject constructor(
    private val historialDao: HistorialDao
) : HistorialRepository {

    override suspend fun guardarHistorial(
        historial: Historial
    ) {

        historialDao.insertarHistorial(
            HistorialEntity(
                nombreModelo = historial.nombreModelo,
                nombreMostrar = historial.nombreMostrar,
                confianza = historial.confianza,
                fecha = historial.fecha,
                imagenUri = historial.imagenUri
            )
        )
    }

    override fun obtenerHistorial(): Flow<List<Historial>> {

        return historialDao
            .obtenerHistorial()
            .map { lista ->
                lista.map { it.toDomain() }
            }
    }

    override fun obtenerHistorialPorEnfermedad(
        nombreModelo: String
    ): Flow<List<Historial>> {

        return historialDao
            .obtenerHistorialPorEnfermedad(nombreModelo)
            .map { lista ->
                lista.map { it.toDomain() }
            }
    }

    override suspend fun eliminarPorId(id: Int) {
        historialDao.eliminarPorId(id)
    }

    override suspend fun eliminarTodo() {
        historialDao.eliminarTodo()
    }
}