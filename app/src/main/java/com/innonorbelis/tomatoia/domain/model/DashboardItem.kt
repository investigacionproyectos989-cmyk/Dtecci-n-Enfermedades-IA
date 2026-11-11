package com.innonorbelis.tomatoia.domain.model

sealed class DashboardItem {

    data class DiagnosticoItem(
        val data: Diagnostico
    ) : DashboardItem()

    // DESCRIPCIÓN GENERAL DE LA ENFERMEDAD
    data class DescripcionItem(
        val descripcion: String
    ) : DashboardItem()

    // LISTA DE SÍNTOMAS
    data class SintomasItem(
        val lista: List<String>
    ) : DashboardItem()

    // RECOMENDACIONES
    data class RecomendacionesItem(
        val lista: List<String>
    ) : DashboardItem()

    //

    data class TratamientoItem(
        val tratamientos: List<Tratamiento>,
        val expanded: Boolean = false
    ) : DashboardItem()

}