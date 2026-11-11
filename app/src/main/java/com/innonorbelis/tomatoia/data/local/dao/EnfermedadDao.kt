package com.innonorbelis.tomatoia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.innonorbelis.tomatoia.data.local.entity.EnfermedadEntity
import com.innonorbelis.tomatoia.data.local.entity.TratamientoEntity
import com.innonorbelis.tomatoia.data.local.relation.EnfermedadConTratamientos

@Dao
interface EnfermedadDao {
    // ENFERMEDAD

    @Query("""
        SELECT * FROM enfermedad
        WHERE nombreModelo = :nombre
        LIMIT 1
    """)
    suspend fun getEnfermedadPorModelo(
        nombre: String
    ): EnfermedadEntity?

    @Query("SELECT COUNT(*) FROM enfermedad")
    suspend fun contarEnfermedades(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnfermedades(
        lista: List<EnfermedadEntity>
    )

 // TRATAMIENTOS

    @Query("""
        SELECT * FROM tratamiento
        WHERE idEnfermedad = :idEnfermedad
    """)
    suspend fun getTratamientos(
        idEnfermedad: Int
    ): List<TratamientoEntity>

    @Query("SELECT COUNT(*) FROM tratamiento")
    suspend fun contarTratamientos(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTratamientos(
        lista: List<TratamientoEntity>
    )
    // DELETE SEEDER

    @Query("DELETE FROM tratamiento")
    suspend fun deleteAllTratamientos()

    @Query("DELETE FROM enfermedad")
    suspend fun deleteAllEnfermedades()

     // DETALLE ENFERMEDAD


    @Transaction
    @Query("""
        SELECT * FROM enfermedad
        WHERE nombreModelo = :nombre
        LIMIT 1
    """)
    suspend fun obtenerDetalleCompleto(
        nombre: String
    ): EnfermedadConTratamientos?
}