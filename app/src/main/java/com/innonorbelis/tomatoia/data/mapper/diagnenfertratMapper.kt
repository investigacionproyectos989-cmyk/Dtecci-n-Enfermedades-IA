package com.innonorbelis.tomatoia.data.mapper

import com.innonorbelis.tomatoia.data.local.entity.EnfermedadEntity
import com.innonorbelis.tomatoia.data.local.entity.HistorialEntity
import com.innonorbelis.tomatoia.data.local.entity.TratamientoEntity
import com.innonorbelis.tomatoia.domain.model.Enfermedad
import com.innonorbelis.tomatoia.domain.model.Historial
import com.innonorbelis.tomatoia.domain.model.Tratamiento

fun EnfermedadEntity.toDomain(): Enfermedad {
    return Enfermedad(
        id = id,
        nombre = nombreMostrar,
        nombreCientifico=nombreCientifico,
        descripcion = descripcion,
        sintomas = sintomas,
        recomendaciones = recomendacionInmediata
    )
}

fun TratamientoEntity.toDomain() = Tratamiento(
    ingredienteActivo = ingredienteActivo,
    producto = producto,
    dosis = dosis,
    pc = periodoCarencia,
    pr = periodoReentrada
)

fun HistorialEntity.toDomain() = Historial(

    id = id,
    nombreModelo=nombreModelo,
    nombreMostrar= nombreMostrar,
    confianza=confianza,
    fecha=fecha,
    imagenUri=imagenUri

)
