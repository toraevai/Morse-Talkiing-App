package com.example.morsetalking.player

import android.media.MediaPlayer
import javax.inject.Inject

class MorsePlayer @Inject constructor(
    private val mediaPlayer: MediaPlayer
) {
    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayerAndSeekToBeginning() {
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }
}