package com.example.morsetalking.ui

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.morsetalking.R
import com.example.morsetalking.camera.MorseCamera
import com.example.morsetalking.data.symbols
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MorseScreenViewModel @Inject constructor(
    private val morseCamera: MorseCamera
) : ViewModel() {

    var message by mutableStateOf("")
        private set

    var dotDuration by mutableStateOf("200")
        private set

    fun changeMessage(text: String) {
        message = text
    }

    fun sendMessage(context: Context) {
        if (dotDuration.isEmpty() || dotDuration.toInt() < 100) {
            Toast.makeText(context, "Длительность не может быть меньше 100мс.", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModelScope.launch {
                for (symbol in message) {
                    if (symbol.toString().contains(" ")) {
                        morseCamera.disableFlashLight()
                        delay(7 * dotDuration.toLong())
                    } else {
                        val morseCode: String = symbols.find {
                            it.symbol.contains(symbol.uppercase())
                        }!!.code
                        for (morseSymbol in morseCode) {
                            if (morseSymbol.toString().contains("0")) {
                                morseCamera.disableFlashLight()
                                delay(dotDuration.toLong())
                            } else {
                                morseCamera.enableFlashLight()
                                delay(dotDuration.toLong())
                            }
                        }
                    }
                    morseCamera.disableFlashLight()
                    delay(dotDuration.toLong() * 3)
                }
            }
        }
    }

    fun sendAudioMessage(context: Context) {
        if (dotDuration.isEmpty() || dotDuration.toInt() < 100) {
            Toast.makeText(context, "Длительность не может быть меньше 100мс.", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModelScope.launch {
                val mediaPlayer = MediaPlayer.create(context, R.raw.dot_sound)
                for (symbol in message) {
                    if (symbol.toString().contains(" ")) {
                        mediaPlayer.pause()
                        delay(7 * dotDuration.toLong())
                    } else {
                        val morseCode: String = symbols.find {
                            it.symbol.contains(symbol.uppercase())
                        }!!.code
                        for (morseSymbol in morseCode) {
                            if (morseSymbol.toString().contains("0")) {
                                mediaPlayer.pause()
                                delay(dotDuration.toLong())
                            } else {
                                mediaPlayer.start()
                                delay(dotDuration.toLong())
                            }
                        }
                    }
                    mediaPlayer.pause()
                    delay(dotDuration.toLong() * 3)
                }
                mediaPlayer.release()
            }
        }
    }

    fun changeDotDuration(duration: String) {
        dotDuration = duration
    }
}