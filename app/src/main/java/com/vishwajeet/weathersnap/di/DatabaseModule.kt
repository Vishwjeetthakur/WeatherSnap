package com.vishwajeet.weathersnap.di

import android.content.Context
import androidx.room.Room
import com.vishwajeet.weathersnap.data.local.WeatherDao
import com.vishwajeet.weathersnap.data.local.WeatherDatabse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabse {
        return Room.databaseBuilder(
            context,
            WeatherDatabse::class.java,
            "weather_snap_dp"
        ).build()
    }

    @Provides
    fun provideWeatherDao(database: WeatherDatabse) : WeatherDao{
        return  database.weatherDao()
    }
}