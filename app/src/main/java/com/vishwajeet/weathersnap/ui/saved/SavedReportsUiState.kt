package com.vishwajeet.weathersnap.ui.saved

import com.vishwajeet.weathersnap.data.local.WeatherReportEntity

sealed interface SavedReportsUiState {
    object Loading : SavedReportsUiState
    data class Success(val reports: List<WeatherReportEntity>) : SavedReportsUiState
    object Empty : SavedReportsUiState
}
