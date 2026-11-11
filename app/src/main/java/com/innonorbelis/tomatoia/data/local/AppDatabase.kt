package com.innonorbelis.tomatoia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.innonorbelis.tomatoia.data.local.dao.EnfermedadDao
import com.innonorbelis.tomatoia.data.local.dao.HistorialDao
import com.innonorbelis.tomatoia.data.local.dao.WeatherDao
import com.innonorbelis.tomatoia.data.local.entity.EnfermedadEntity
import com.innonorbelis.tomatoia.data.local.entity.HistorialEntity
import com.innonorbelis.tomatoia.data.local.entity.TratamientoEntity
import com.innonorbelis.tomatoia.data.local.entity.WeatherEntity

@Database(entities = [
    WeatherEntity::class,
    EnfermedadEntity::class,
    TratamientoEntity::class,
    HistorialEntity::class
    ],

    version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun enfermedadDao(): EnfermedadDao
    abstract fun historialDao(): HistorialDao


}
