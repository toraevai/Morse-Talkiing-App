package com.example.morsetalking.ui

import android.util.Log
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "MyApp"

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

    private var sendSOSMessage = true
        private set

    fun changeMessage(text: String) {
        userMessage = text
    }

    /*fun changeSendingOfSOS() {
        sendSOSMessage = !sendSOSMessage
    }*/

    fun changeAudioSending() {
        sendAudioMessage = !sendAudioMessage
    }

    fun changeVisibleSending() {
        sendVisibleMessage = !sendVisibleMessage
    }

    fun sendUserMessage() {
        when {
            sendAudioMessage && sendVisibleMessage -> {
                sendAudioMessage(userMessage, false)
                sendVisibleMessage(userMessage, false)
            }

            sendAudioMessage -> sendAudioMessage(userMessage, false)
            sendVisibleMessage -> sendVisibleMessage(userMessage, false)
        }
    }

    fun sendSOS() {
        val message = "SOS"
        sendSOSMessage = !sendSOSMessage
        Log.d(TAG, "send sos message = $sendSOSMessage")
        when {
            sendAudioMessage && sendVisibleMessage -> {
                sendAudioMessage(message, sendSOSMessage)
                sendVisibleMessage(message, sendSOSMessage)
            }

            sendAudioMessage -> sendAudioMessage(message, sendSOSMessage)
            sendVisibleMessage -> sendVisibleMessage(message, sendSOSMessage)
        }
    }

    private fun sendVisibleMessage(message: String, sendCyclically: Boolean) {
        if (checkConditionsForSendMessage()) {
            viewModelScope.launch {
                if (sendCyclically) {
                    while (true) {
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
                        Log.d(TAG, "send cyclically = $sendCyclically")
                    }
                } else this.cancel()
            }
        }
    }

    private fun sendAudioMessage(message: String, sendCyclically: Boolean) {
        if (checkConditionsForSendMessage()) {
            viewModelScope.launch {
                do {
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
                } while (sendCyclically)
            }
        }
    }

    fun changeDotDuration(duration: String) {
        dotDuration = duration
    }

    private fun checkConditionsForSendMessage(): Boolean {
        return if (dotDuration.isEmpty() || dotDuration.toInt() < 10) {
            viewModelScope.launch { snackbarHostState.showSnackbar("Длительность не может быть меньше 10мс.") }
            false
        } else true
    }

    override fun onCleared() {
        super.onCleared()
        morsePlayer.releasePlayer()
    }
}