package com.innonorbelis.tomatoia.core.util

fun traducir(label: String): String {
    return when(label) {
        "Bacterial_spot" -> "Mancha bacteriana"
        "Early_blight" -> "Tizón temprano"
        "Late_blight" -> "Tizón tardío"
        "Leaf_Mold" -> "Moho de la hoja"
        "Septoria_leaf_spot" -> "Mancha por Septoria"
        "Target_Spot" -> "Mancha foliar o de hierro"
        "healthy" -> "Hoja Sana"
        "otros"->""
        "powdery_mildew" -> "Mildiu polvoriento"

        else -> label


    }
}
