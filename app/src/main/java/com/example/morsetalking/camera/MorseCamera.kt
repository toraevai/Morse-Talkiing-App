package com.example.morsetalking.camera

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.IOException

class MorseCamera(private val cameraManager: CameraManager) {

    private val cameraId = cameraManager.cameraIdList[0]
    val camera: Camera = Camera.open()
    val parameters = camera.parameters

    fun enableFlashLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager.setTorchMode(cameraId, true)
        } else {
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera.parameters = parameters
            val texture = SurfaceTexture(0)
            try {
                camera.setPreviewTexture(texture)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            camera.startPreview()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun disableFlashLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager.setTorchMode(cameraId, false)
        } else {
            parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
            camera.stopPreview()
            camera.release()
        }
    }
}