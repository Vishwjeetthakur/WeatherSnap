package com.vishwajeet.weathersnap.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishwajeet.weathersnap.data.remote.dto.CityDto
import com.vishwajeet.weathersnap.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {

        _uiState.update { it.copy(searchQuery = query) }

        if (query.length > 2) {
            searchJob?.cancel()
            _uiState.update {
                it.copy(
                    isSearching = true
                )
            }
            searchJob = viewModelScope.launch {
                delay(500)
                try {
                    val response = repository.getCitySuggestions(query)
                    _uiState.update { it.copy(suggestions = response.results ?: emptyList(),
                        isSearching = false
                    ) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = "Error: Failed To Fetch City ${e.message}",
                        isSearching = false
                    ) }
                }
            }
        } else {
            _uiState.update { it.copy(suggestions = emptyList(),
                isSearching = false) }

        }
    }

    fun selectCity(city: CityDto) {
        _uiState.update { it.copy(selectCity = city, suggestions = emptyList(), isLoading = true) }
        viewModelScope.launch {
            try {
                val response = repository.getWeatherData(city.latitude, city.longitude)
                _uiState.update { it.copy(weatherInfo = response, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error: Failed To Fetch Weather ${e.message}", isLoading = false) }
            }
        }

    }

}