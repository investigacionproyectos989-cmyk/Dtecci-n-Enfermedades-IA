package com.innonorbelis.tomatoia.data.local.seeder

import com.innonorbelis.tomatoia.data.local.dao.EnfermedadDao
import javax.inject.Inject



class DatabaseSeeder @Inject constructor(
    private val dao: EnfermedadDao,
    private val prefs: SeederPreferences
) {

    suspend fun seed() {

        val currentVersion = prefs.getVersion()
        val SEED_VERSION = 3
        if (currentVersion >= SEED_VERSION) return

        //  LIMPIAR BD SOLO SI CAMBIÓ VERSION
        dao.deleteAllEnfermedades()
        dao.deleteAllTratamientos()

        //  INSERTAR NUEVOS DATOS
        DatabaseSeederEnfermedad.seedEnfermedad(dao)
        DatabaseSeederTratamiento.seedTratamientos(dao)

        prefs.saveVersion(SEED_VERSION)
    }
}
