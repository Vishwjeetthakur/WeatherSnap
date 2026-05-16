package com.vishwajeet.weathersnap.ui.saved


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishwajeet.weathersnap.data.local.WeatherReportEntity
import com.vishwajeet.weathersnap.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject



@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val uiState: StateFlow<SavedReportsUiState> = repository.getAllReports()
        .map { reports ->
            if (reports.isEmpty()) SavedReportsUiState.Empty
            else SavedReportsUiState.Success(reports)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavedReportsUiState.Loading
        )
}