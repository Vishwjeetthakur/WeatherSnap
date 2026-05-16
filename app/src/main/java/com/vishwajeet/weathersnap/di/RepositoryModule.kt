package com.vishwajeet.weathersnap.di

import com.vishwajeet.weathersnap.data.local.WeatherDao
import com.vishwajeet.weathersnap.data.remote.WeatherApi
import com.vishwajeet.weathersnap.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi, dao: WeatherDao): WeatherRepository {
        return WeatherRepository(api, dao)
    }
}