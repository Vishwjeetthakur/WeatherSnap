package com.vishwajeet.weathersnap.ui.report

import android.net.Uri
import java.io.File

data class CreateReportUiState(
    val notes: String = "",
    val originalImageUri: Uri? = null,
    val compressedFile: File? = null,
    val originalSize: Long = 0,
    val compressedSize: Long = 0,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)
