package com.vishwajeet.weathersnap.data.remote

import com.vishwajeet.weathersnap.data.remote.dto.CitySearchResponse
import com.vishwajeet.weathersnap.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun getCitySuggestions(
        @Query("name") cityName: String,
        @Query("count") count: Int =10,
    ): CitySearchResponse


    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current : String = "temperature_2m,relative_humidity_2m,weather_code,pressure_msl,wind_speed_10m"
    ): WeatherResponse


}