package com.innonorbelis.tomatoia.data.local.seeder

import com.innonorbelis.tomatoia.data.local.dao.EnfermedadDao
import com.innonorbelis.tomatoia.data.local.entity.EnfermedadEntity


object DatabaseSeederEnfermedad {

    suspend fun seedEnfermedad(dao: EnfermedadDao) {

        // EVITAR DUPLICADOS
        val count = dao.contarEnfermedades()
        if (count > 0) return

        val enfermedades = listOf(

            EnfermedadEntity(
                nombreModelo = "Early_blight",
                nombreMostrar = "Tizón temprano",
                nombreCientifico = "Alternaria solani",
                descripcion = "Enfermedad fúngica que afecta hojas, tallos y frutos.",
                sintomas = """
Manchas con círculos uno dentro de otro
Empieza en las hojas de abajo
Amarillamiento alrededor de las lesiones
La hoja se pone amarilla y cae de forma progresiva
Lesiones en tallos y frutos
                """.trimIndent(),
                recomendacionInmediata = """
Eliminar hojas infectadas
Evitar riego por aspersión
Mejorar ventilación

                """.trimIndent()
            ),

            EnfermedadEntity(
                nombreModelo = "Late_blight",
                nombreMostrar = "Tizón tardío",
                nombreCientifico = "Phytophthora infestans",
                descripcion = "Enfermedad devastadora en condiciones húmedas.",
                sintomas = """
Manchas que parecen quemaduras de color marrón
Si hay humedad, sale un algodón blanco detrás de la hoja
Marchitez rápiday tallos negros.
                """.trimIndent(),
                recomendacionInmediata = """
Quita y quema las plantas enfermas.
No mojes las hojas al regar

                """.trimIndent()
            ),

            EnfermedadEntity(
                nombreModelo = "Septoria_leaf_spot",
                nombreMostrar = "Mancha de Septoria",
                nombreCientifico = "Septoria lycopersici",
                descripcion = "Enfermedad foliar común.",
                sintomas = """
Numerosas manchas circulares pequeñas
Bordes oscuros y centros grises
Dentro de las manchas se ven unos puntitos negros diminutos
Caída de hojas
                """.trimIndent(),
                recomendacionInmediata = """
Eliminar hojas afectadas
Limpiar las herramientas de poda
No trabajar el cultivo cuando las hojas estén mojadas
                """.trimIndent()
            ),

            EnfermedadEntity(
                nombreModelo = "Bacterial_spot",
                nombreMostrar = "Mancha bacteriana",
                nombreCientifico = "Xanthomonas spp.",
                descripcion = "Enfermedad bacteriana en hojas y frutos.",
                sintomas = """
Manchas negras pequeñas
Lesiones húmedo o "grasoso
Rodeada  por un circulo amarillo
Frutos con costras
                """.trimIndent(),
                recomendacionInmediata = """
Evitar salpicaduras
Eliminar plantas infectadas
                """.trimIndent()
            ),

            EnfermedadEntity(
                nombreModelo = "Leaf_Mold",
                nombreMostrar = "Moho de la hoja",
                nombreCientifico = "Passalora fulva",
                descripcion = "Aparece con alta humedad.",
                sintomas = """
Manchas amarillas arriba 
Pelusa verde oscuro por debajo de la hoja.
Caída de hojas
                """.trimIndent(),
                recomendacionInmediata = """
Reducir humedad
Mejorar ventilación
                """.trimIndent()
            ),
            EnfermedadEntity(
                nombreModelo = "Target_Spot",
                nombreMostrar = "Mancha de hierro",
                nombreCientifico = "Cercospora",
                descripcion = "",
                sintomas = """
Manchas pequeñas
Circulares y numerosas 
Centros claros y bordes oscuros 
                """.trimIndent(),
                recomendacionInmediata = """
Reducir humedad
                """.trimIndent()
            ),
            EnfermedadEntity(
                nombreModelo = "powdery_mildew",
                nombreMostrar = "Mildeo polvoroso",
                nombreCientifico = "Oídio",
                descripcion = "",
                sintomas = """
Un polvo blanco o grisáceo que cubre las hojas, tallos y frutos,como si les hubieran caído harina encima.
Las hojas se deforman y pueden secarse.
                """.trimIndent(),
                recomendacionInmediata = """
Mejorar ventilación
                """.trimIndent()
            ),
            EnfermedadEntity(
                nombreModelo = "Gray_mold",
                nombreMostrar = "Moho gris (Botrytis)",
                nombreCientifico = "Botrytis cinerea",
                descripcion = "",
                sintomas = """
Manchas pequeñas, negras y con aspecto húmedo o "grasoso".
rodeada  por un circulo amarillo. 
Frutos con costras pequeñas y ásperas.
                """.trimIndent(),
                recomendacionInmediata = """
Reducir humedad
Mejorar ventilación
                """.trimIndent()
            ),

           /* EnfermedadEntity(
                nombreModelo = "Yellow_Leaf_Curl_Virus",
                nombreMostrar = "Virus hoja amarilla",
                nombreCientifico = "TYLCV",
                descripcion = "Virus transmitido por mosca blanca.",
                sintomas = """
Hojas enrolladas
Color amarillo
Retraso crecimiento
                """.trimIndent(),
                recomendacionInmediata = """
Controlar vector
Eliminar plantas infectadas
                """.trimIndent()
            ),*/

            EnfermedadEntity(
                nombreModelo = "healthy",
                nombreMostrar = "Planta sana",
                nombreCientifico = null,
                descripcion = "Sin enfermedad.",
                sintomas = """
Hojas verdes
Crecimiento normal
                """.trimIndent(),
                recomendacionInmediata = """
Mantener monitoreo
Buenas prácticas agrícolas
                """.trimIndent()
            )
        )

        dao.insertEnfermedades(enfermedades)
    }
}