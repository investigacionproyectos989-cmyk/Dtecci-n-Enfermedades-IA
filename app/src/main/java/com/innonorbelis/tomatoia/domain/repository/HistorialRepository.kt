package com.innonorbelis.tomatoia.domain.repository

import com.innonorbelis.tomatoia.domain.model.Historial
import kotlinx.coroutines.flow.Flow

interface HistorialRepository {

    suspend fun guardarHistorial(
        historial: Historial
    )

    fun obtenerHistorial(): Flow<List<Historial>>

    fun obtenerHistorialPorEnfermedad(
        nombreModelo: String
    ): Flow<List<Historial>>

    suspend fun eliminarPorId(id: Int)

    suspend fun eliminarTodo()
}