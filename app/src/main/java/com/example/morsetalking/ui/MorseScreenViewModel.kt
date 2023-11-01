package com.example.morsetalking.ui

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.morsetalking.MorseTalkingApp
import com.example.morsetalking.camera.MorseCamera
import com.example.morsetalking.data.symbols
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MorseScreenViewModel(
    private val morseCamera: MorseCamera
) : ViewModel() {
    var message by mutableStateOf("")
    var dotDuration: Long by mutableStateOf(200)

    @RequiresApi(Build.VERSION_CODES.M)
    fun sendMessage() {
        viewModelScope.launch {
            for (symbol in message) {
                if (symbol.toString().contains(" ")) {
                    morseCamera.disableFlashLight()
                    delay(7 * dotDuration)
                } else {
                    val morseCode: String = symbols.find {
                        it.symbol.contains(symbol.uppercase())
                    }!!.code
                    for (morseSymbol in morseCode) {
                        if (morseSymbol.toString().contains("0")) {
                            morseCamera.disableFlashLight()
                            delay(dotDuration)
                        } else {
                            morseCamera.enableFlashLight()
                            delay(dotDuration)
                        }
                    }
                }
                morseCamera.disableFlashLight()
                delay(dotDuration * 3)
            }
        }
    }

    fun changeDotDuration(duration: Long, context: Context) {
        dotDuration = if (duration < 100) {
            Toast.makeText(context, "Длительность не может быть меньше 100мс.", Toast.LENGTH_SHORT)
                .show()
            100
        } else duration
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MorseTalkingApp)
                MorseScreenViewModel(application.morseCamera)
            }
        }
    }
}