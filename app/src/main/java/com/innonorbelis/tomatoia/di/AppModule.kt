package com.innonorbelis.tomatoia.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.innonorbelis.tomatoia.data.local.AppDatabase
import com.innonorbelis.tomatoia.data.local.dao.EnfermedadDao
import com.innonorbelis.tomatoia.data.local.dao.HistorialDao
import com.innonorbelis.tomatoia.data.local.dao.WeatherDao
import com.innonorbelis.tomatoia.data.remote.api.WeatherApi
import com.innonorbelis.tomatoia.data.repository.EnfermedadRepositoryImpl
import com.innonorbelis.tomatoia.data.repository.HistorialRepositoryImpl
import com.innonorbelis.tomatoia.data.repository.WeatherRepositoryImpl
import com.innonorbelis.tomatoia.domain.repository.EnfermedadRepository
import com.innonorbelis.tomatoia.domain.repository.HistorialRepository
import com.innonorbelis.tomatoia.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // DATABASE

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext ctx: Context
    ): AppDatabase {

        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS historial (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombreModelo TEXT NOT NULL,
                        nombreMostrar TEXT NOT NULL,
                        confianza INTEGER NOT NULL,
                        fecha INTEGER NOT NULL,
                        imagenUri TEXT
                    )
                """.trimIndent())
            }
        }

        return Room.databaseBuilder(
            ctx,
            AppDatabase::class.java,
            "tomatoia_db"
        )
            .addMigrations(migration1to2)
            .fallbackToDestructiveMigration()
            .build()
    }


    // DAOs

    @Provides
    fun provideWeatherDao(
        db: AppDatabase
    ): WeatherDao =
        db.weatherDao()

    @Provides
    fun provideEnfermedadDao(
        db: AppDatabase
    ): EnfermedadDao =
        db.enfermedadDao()

    @Provides
    fun provideHistorialDao(
        db: AppDatabase
    ): HistorialDao =
        db.historialDao()


    // RETROFIT

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {

        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApi =
        retrofit.create(WeatherApi::class.java)


    // REPOSITORIES

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        dao: WeatherDao
    ): WeatherRepository =
        WeatherRepositoryImpl(api, dao)

    @Provides
    @Singleton
    fun provideEnfermedadRepository(
        dao: EnfermedadDao
    ): EnfermedadRepository =
        EnfermedadRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideHistorialRepository(
        historialDao: HistorialDao
    ): HistorialRepository =
        HistorialRepositoryImpl(historialDao)
}