package com.innonorbelis.tomatoia.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tratamiento",
    foreignKeys = [
        ForeignKey(
            entity = EnfermedadEntity::class,
            parentColumns = ["id"],
            childColumns = ["idEnfermedad"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TratamientoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idEnfermedad: Int,
    val ingredienteActivo: String,
    val producto: String,
    val dosis: String,
    val periodoCarencia: Int,
    val periodoReentrada: Int


)