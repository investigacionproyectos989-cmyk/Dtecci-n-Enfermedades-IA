package com.innonorbelis.tomatoia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.innonorbelis.tomatoia.data.local.entity.HistorialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarHistorial(
        historial: HistorialEntity
    )

    @Query("""
        SELECT * FROM historial
        ORDER BY fecha DESC
    """)
    fun obtenerHistorial(): Flow<List<HistorialEntity>>

    @Query("""
        SELECT * FROM historial
        WHERE nombreModelo = :nombreModelo
        ORDER BY fecha DESC
    """)
    fun obtenerHistorialPorEnfermedad(
        nombreModelo: String
    ): Flow<List<HistorialEntity>>

    @Query("DELETE FROM historial WHERE id = :id")
    suspend fun eliminarPorId(id: Int)

    @Query("DELETE FROM historial")
    suspend fun eliminarTodo()
}