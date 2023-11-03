package com.example.morsetalking.camera

import android.hardware.camera2.CameraManager
import javax.inject.Inject

class MorseCamera @Inject constructor(
    private val cameraManager: CameraManager
) {

    private val cameraId = cameraManager.cameraIdList[0]

    fun enableFlashLight() {
        cameraManager.setTorchMode(cameraId, true)
    }

    fun disableFlashLight() {
        cameraManager.setTorchMode(cameraId, false)
    }
}