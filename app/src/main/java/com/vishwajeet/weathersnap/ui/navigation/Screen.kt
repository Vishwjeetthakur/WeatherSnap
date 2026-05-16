package com.vishwajeet.weathersnap.ui.navigation

import kotlinx.serialization.Serializable


sealed class Screen {

    @Serializable
    data object WeatherRoute : Screen()

    @Serializable
    data object SavedReportsRoute : Screen()

    @Serializable
    data object CameraRoute : Screen()

    @Serializable
    data class CreateReportRoute(
        val cityName: String,
        val temp: Double,
        val humidity: Int,
        val condition: String,
        val wind: Double,
        val pressure: Double
    ) : Screen()
}