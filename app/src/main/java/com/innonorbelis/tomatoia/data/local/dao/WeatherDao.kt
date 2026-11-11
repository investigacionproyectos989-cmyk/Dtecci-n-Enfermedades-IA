package com.innonorbelis.tomatoia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.innonorbelis.tomatoia.data.local.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_table LIMIT 1")
    suspend fun getWeather(): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)
//Borrar antes de insertar
    @Query("DELETE FROM weather_table")
    suspend fun clearWeather()
}
