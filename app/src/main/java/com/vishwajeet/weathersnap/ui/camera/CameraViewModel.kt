package com.vishwajeet.weathersnap.ui.camera

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private var _controller: LifecycleCameraController? = null

    fun getController(context: Context): LifecycleCameraController {
        return _controller ?: LifecycleCameraController(context).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE
            )
            _controller = this
        }
    }
}