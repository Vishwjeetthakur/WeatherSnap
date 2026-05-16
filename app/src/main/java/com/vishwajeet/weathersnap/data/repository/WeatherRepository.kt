package com.vishwajeet.weathersnap.data.repository

import com.vishwajeet.weathersnap.data.local.WeatherDao
import com.vishwajeet.weathersnap.data.local.WeatherReportEntity
import com.vishwajeet.weathersnap.data.remote.WeatherApi
import com.vishwajeet.weathersnap.data.remote.dto.CitySearchResponse
import com.vishwajeet.weathersnap.data.remote.dto.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api : WeatherApi,
    private val dao: WeatherDao
) {

    suspend fun getCitySuggestions(cityName: String) : CitySearchResponse{
        return withContext(Dispatchers.IO){
            api.getCitySuggestions(cityName)
        }
    }

    suspend fun getWeatherData(lat: Double , lon: Double): WeatherResponse{
        return withContext(Dispatchers.IO){
            api.getCurrentWeather(lat,lon)
        }
    }

    suspend fun saveWeatherReports(report: WeatherReportEntity){
        withContext(Dispatchers.IO){
            dao.insertWeatherReport(report)
        }
    }

    fun getAllReports() : Flow<List<WeatherReportEntity>> {
        return dao.getAllReports()
    }



}