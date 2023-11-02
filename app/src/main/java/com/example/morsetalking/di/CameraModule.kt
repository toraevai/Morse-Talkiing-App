package com.example.morsetalking.di

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CameraModule {

    @Provides
    @Singleton
    fun provideCamera(@ApplicationContext appContext: Context): CameraManager {
        return appContext.getSystemService(Application.CAMERA_SERVICE) as CameraManager
    }
}