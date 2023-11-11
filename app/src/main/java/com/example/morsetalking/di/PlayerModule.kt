package com.example.morsetalking.di

import android.content.Context
import android.media.MediaPlayer
import com.example.morsetalking.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PlayerModule {

    @Provides
    @Singleton
    fun providePlayer(@ApplicationContext appContext: Context): MediaPlayer {
        val mediaPlayer = MediaPlayer.create(appContext, R.raw.dot_sound_1sec)
        mediaPlayer.isLooping = true
        return mediaPlayer
    }
}