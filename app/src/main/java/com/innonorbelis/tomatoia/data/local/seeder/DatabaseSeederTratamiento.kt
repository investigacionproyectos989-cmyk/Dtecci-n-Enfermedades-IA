package com.innonorbelis.tomatoia.data.local.seeder

import com.innonorbelis.tomatoia.data.local.dao.EnfermedadDao
import com.innonorbelis.tomatoia.data.local.entity.TratamientoEntity

object DatabaseSeederTratamiento {

        suspend fun seedTratamientos(dao: EnfermedadDao) {

            val count = dao.contarTratamientos()
            if (count > 0) return

            val late = dao.getEnfermedadPorModelo("Late_blight")?: return

            val tratamientosLate = listOf(
                TratamientoEntity(0, late.id, "Clorotalonil", "Daconil", "2 – 3cc/L ", 7, 4),
                TratamientoEntity(0, late.id, "Mancozeb", "Antracol", "2 – 3g/L ", 7, 4),
                TratamientoEntity(0, late.id, "Mancozeb", "Manzate", "2 – 2.5g/L", 7, 4),
                TratamientoEntity(0, late.id, "Propamocarb", "Infinito", "1.5 – 2cc/L", 7, 4),
                TratamientoEntity(0, late.id, "Metalaxil + Cobre", "Fitoraz WP76", "1.5 – 2cc/L", 14, 4),
                TratamientoEntity(0, late.id, "Dimetomorf", "Forum", "1 – 1.5g/L", 7, 4)
            )


            val early = dao.getEnfermedadPorModelo("Early_blight")?: return

            val tratamientosEarly = listOf(
                TratamientoEntity(0, early.id, "Azoxistrobina", "Amistar Top", "1cc/L", 7, 4),
                TratamientoEntity(0, early.id, "Piraclostrobina", "Cabrio Top", "1.5 – 2g/L", 7, 4),
                TratamientoEntity(0, early.id, "Difenoconazol", "Score", "1cc/L", 7, 4),
                TratamientoEntity(0, early.id, "Difenoconazol", "Opera", "1cc/L", 20, 12),
                TratamientoEntity(0, early.id, "Difenoconazol", "Nativo", "1cc/L", 8, 4),
                TratamientoEntity(0, early.id, "Difenoconazol + Cyprodinil", "Inspire Gol", "1cc/L", 7, 4)
            )


            val oidio = dao.getEnfermedadPorModelo("powdery_mildew")?: return

            val tratamientosOidio = listOf(
                TratamientoEntity(0, oidio.id, "Difenoconazol", "Score", "1cc/L", 7, 4),
                TratamientoEntity(0, oidio.id, "Azoxistrobina", "Amistar Top", "1.25cc/L", 7, 4)
            )

            val cercospora = dao.getEnfermedadPorModelo("Target_Spot")?: return

            val tratamientosCercospora = listOf(
                TratamientoEntity(0, cercospora.id, "Azoxistrobina", "Amistar Top", "1 cc/L", 7, 4),
                TratamientoEntity(0, cercospora.id, "Azoxistrobina", "Amistar Ztra", "1 cc/L", 15, 4),
                TratamientoEntity(0, cercospora.id, "Clorotalonil", "Daconil", "2 – 3 cc/L", 7, 4)
            )
            val mold = dao.getEnfermedadPorModelo("Leaf_Mold")?: return

            val tratamientosMold = listOf(
                TratamientoEntity(0, mold.id, "Clorotalonil", "Daconil", "2.5 – 3.0 cc/L", 7, 4),
                TratamientoEntity(0, mold.id, "Mancozeb", "Manzate", "2 – 2.5 g/L", 7, 4),
                TratamientoEntity(0, mold.id, "Azoxistrobina", "Amistar Top", "1 cc/L", 7, 4),
                TratamientoEntity(0, mold.id, "Difenoconazol", "Score", "1 ml/L", 7, 4)
            )
            val septoria = dao.getEnfermedadPorModelo("Septoria_leaf_spot")?: return

            val tratamientosSeptoria = listOf(
                TratamientoEntity(0, septoria.id, "Clorotalonil", "Daconil", "1 – 3 cc/L", 7, 4),
                TratamientoEntity(0, septoria.id, "Mancozeb", "Manzate", "2 – 2.5 g/L", 7, 4),
                TratamientoEntity(0, septoria.id, "Azoxistrobina", "Amistar Top", "1 cc/L ", 7, 4)
            )
            val bacterial = dao.getEnfermedadPorModelo("Bacterial_spot")?: return

            val tratamientosBacterial = listOf(
                TratamientoEntity(0, bacterial.id, "Oxicloruro de Cobre", "Oxicloruro de Cobre", "2 – 3 g/L agua", 3, 4),
                TratamientoEntity(0, bacterial.id, "Hidróxido de Cobre", "Hidróxido de Cobre", "1.5 – 2 g/L", 7, 4)
            )
            val botrytis = dao.getEnfermedadPorModelo("Gray_mold")?: return

            val tratamientosBotrytis = listOf(
                TratamientoEntity(0, botrytis.id, "Boscalid + Piraclostrobina", "Luna Tranquility", "1 cc/L", 7, 4),
                TratamientoEntity(0, botrytis.id, "Difenoconazol + Cyprodinil", "Inspire Gol", "1 cc/L", 7, 4),
                TratamientoEntity(0, botrytis.id, "Captan", "Ortocide", "2 g/L", 7, 24)
            )



            dao.insertTratamientos(
                tratamientosLate +
                        tratamientosEarly +
                        tratamientosOidio +
                        tratamientosCercospora +
                        tratamientosMold +
                        tratamientosSeptoria +
                        tratamientosBacterial+
                        tratamientosBotrytis
            )

    }
}