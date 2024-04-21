package com.example.tensorflow

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.tensorflow.data.TfLiteLandmarkClassifier
import com.example.tensorflow.domain.Classification
import com.example.tensorflow.presentation.LandmarkImageAnalyzer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyViewModel(
    applicationContext : Context
) : ViewModel() {
    private val _classifications = MutableStateFlow<List<Classification>>(emptyList())
    val classifications = _classifications.asStateFlow().value

    private val _analyzer = MutableStateFlow<LandmarkImageAnalyzer>(LandmarkImageAnalyzer(
        TfLiteLandmarkClassifier(
            applicationContext
        ),
        onResult = {
            _classifications.value = it
        }
    ))
    val analyzer = _analyzer.asStateFlow().value

    private val _controller = MutableStateFlow<LifecycleCameraController>(LifecycleCameraController(applicationContext).apply {
        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(applicationContext),
            _analyzer.value
        )
    })
    val controller = _controller.asStateFlow().value

}