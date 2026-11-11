package com.innonorbelis.tomatoia.data.local.relation


import androidx.room.Embedded
import androidx.room.Relation
import com.innonorbelis.tomatoia.data.local.entity.EnfermedadEntity
import com.innonorbelis.tomatoia.data.local.entity.TratamientoEntity

data class EnfermedadConTratamientos(

    @Embedded
    val enfermedad: EnfermedadEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "idEnfermedad"
    )
    val tratamientos: List<TratamientoEntity>
)