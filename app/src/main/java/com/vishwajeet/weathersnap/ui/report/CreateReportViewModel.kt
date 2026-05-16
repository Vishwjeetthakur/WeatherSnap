package com.vishwajeet.weathersnap.ui.report


import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishwajeet.weathersnap.data.local.WeatherReportEntity
import com.vishwajeet.weathersnap.data.repository.WeatherRepository
import com.vishwajeet.weathersnap.ui.navigation.Screen
import com.vishwajeet.weathersnap.utils.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val savedNotes = savedStateHandle.get<String>("notes_key") ?: ""
        val savedImgUri = savedStateHandle.get<String>("img_uri_key")?.let { Uri.parse(it) }
        val savedCompPath = savedStateHandle.get<String>("comp_path_key")?.let { File(it) }
        val origSize = savedStateHandle.get<Long>("orig_size_key") ?: 0L
        val compSize = savedStateHandle.get<Long>("comp_size_key") ?: 0L

        _uiState.update {
            it.copy(
                notes = savedNotes,
                originalImageUri = savedImgUri,
                compressedFile = savedCompPath,
                originalSize = origSize,
                compressedSize = compSize
            )
        }
    }

    fun onNotesChange(newNotes: String) {
        _uiState.update { it.copy(notes = newNotes) }
        savedStateHandle["notes_key"] = newNotes
    }

    fun processCapturedImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            val originalFile = File(uri.path ?: "")
            val origSize = ImageUtils.getFileSize(originalFile)

            val compFile = ImageUtils.compressImage(context, uri)
            val compSize = compFile?.let { ImageUtils.getFileSize(it) } ?: 0L

            _uiState.update {
                it.copy(
                    originalImageUri = uri,
                    compressedFile = compFile,
                    originalSize = origSize,
                    compressedSize = compSize
                )
            }

            savedStateHandle["img_uri_key"] = uri.toString()
            savedStateHandle["comp_path_key"] = compFile?.absolutePath
            savedStateHandle["orig_size_key"] = origSize
            savedStateHandle["comp_size_key"] = compSize


            ImageUtils.deleteFile(originalFile)
        }
    }

    fun saveReport(weatherData: Screen.CreateReportRoute) {
        val currentState = _uiState.value
        if (currentState.compressedFile == null || currentState.isSaving) return

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val entity = WeatherReportEntity(
                cityName = weatherData.cityName,
                temperature = weatherData.temp,
                condition = weatherData.condition,
                humidity = weatherData.humidity,
                windSpeed = weatherData.wind,
                pressure = 0.0,
                imagePath = currentState.compressedFile.absolutePath,
                notes = currentState.notes,
                originalSize = currentState.originalSize,
                compressedSize = currentState.compressedSize
            )

            repository.saveWeatherReports(entity)
            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
        }
    }
}