package com.example.morsetalking

import android.app.Application
import android.hardware.camera2.CameraManager
import com.example.morsetalking.camera.MorseCamera

class MorseTalkingApp: Application() {
    lateinit var morseCamera: MorseCamera
    override fun onCreate() {
        super.onCreate()
        morseCamera = MorseCamera(getSystemService(CAMERA_SERVICE) as CameraManager)
    }
}