package com.vishwajeet.weathersnap.ui.weather

import com.vishwajeet.weathersnap.data.remote.dto.CityDto
import com.vishwajeet.weathersnap.data.remote.dto.WeatherResponse

data class WeatherUiState(
    val searchQuery: String = "",
    val suggestions: List<CityDto> = emptyList(),
    val selectCity: CityDto? = null,
    val weatherInfo: WeatherResponse? = null,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val error: String? = null
)
