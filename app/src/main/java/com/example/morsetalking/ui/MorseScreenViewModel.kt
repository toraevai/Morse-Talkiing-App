package com.example.morsetalking.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.morsetalking.camera.MorseCamera
import com.example.morsetalking.data.symbols
import com.example.morsetalking.player.MorsePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MorseScreenViewModel @Inject constructor(
    private val morseCamera: MorseCamera,
    private val morsePlayer: MorsePlayer
) : ViewModel() {

    var userMessage by mutableStateOf("")
        private set

    var dotDuration by mutableStateOf("100")
        private set

    var sendAudioMessage by mutableStateOf(false)
        private set

    var sendVisibleMessage by mutableStateOf(true)
        private set

    val snackbarHostState = SnackbarHostState()

    fun changeMessage(text: String) {
        userMessage = text
    }

    fun changeAudioSending() {
        sendAudioMessage = !sendAudioMessage
    }

    fun changeVisibleSending() {
        sendVisibleMessage = !sendVisibleMessage
    }

    fun sendUserMessage() {
        when {
            sendAudioMessage && sendVisibleMessage -> {
                sendAudioMessage(userMessage)
                sendVisibleMessage(userMessage)
            }
            sendAudioMessage -> sendAudioMessage(userMessage)
            sendVisibleMessage -> sendVisibleMessage(userMessage)
        }
    }

    fun sendSOS() {
        val message = "SOS"
        when {
            sendAudioMessage && sendVisibleMessage -> {
                sendAudioMessage(message)
                sendVisibleMessage(message)
            }
            sendAudioMessage -> sendAudioMessage(message)
            sendVisibleMessage -> sendVisibleMessage(message)
        }
    }

    private fun sendVisibleMessage(message: String) {
        if (checkConditionsForSendMessage()) {
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

    private fun sendAudioMessage(message: String) {
        if (checkConditionsForSendMessage()) {
            viewModelScope.launch {
                for (symbol in message) {
                    if (symbol.toString().contains(" ")) {
                        morsePlayer.pausePlayerAndSeekToBeginning()
                        delay(7 * dotDuration.toLong())
                    } else {
                        val morseCode: String = symbols.find {
                            it.symbol.contains(symbol.uppercase())
                        }!!.code
                        for (morseSymbol in morseCode) {
                            if (morseSymbol.toString().contains("0")) {
                                morsePlayer.pausePlayerAndSeekToBeginning()
                                delay(dotDuration.toLong())
                            } else {
                                morsePlayer.startPlayer()
                                delay(dotDuration.toLong())
                            }
                        }
                    }
                    morsePlayer.pausePlayerAndSeekToBeginning()
                    delay(dotDuration.toLong() * 3)
                }
            }
        }
    }

    fun changeDotDuration(duration: String) {
        dotDuration = duration
    }

    private fun checkConditionsForSendMessage(): Boolean {
        return if (dotDuration.isEmpty() || dotDuration.toInt() < 100) {
            viewModelScope.launch { snackbarHostState.showSnackbar("Длительность не может быть меньше 100мс.") }
            false
        } else true
    }

    override fun onCleared() {
        super.onCleared()
        morsePlayer.releasePlayer()
    }
}