package com.example.morsetalking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.morsetalking.ui.theme.MorseTalkingTheme

@Composable
fun MorseApp(
    viewModel: MorseScreenViewModel
) {
    val context = LocalContext.current
    MorseScreen(
        message = viewModel.message,
        dotDuration = viewModel.dotDuration,
        onMessageChange = { viewModel.changeMessage(it) },
        onClick = { viewModel.sendAudioMessage(context) },
        onDotDurationChange = { viewModel.changeDotDuration(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseScreen(
    message: String,
    dotDuration: String,
    onMessageChange: (String) -> Unit,
    onClick: () -> Unit,
    onDotDurationChange: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(all = 10.dp)
    ) {
        TextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text(text = "Введите сообщение") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(onClick = onClick) {
            Text("Отправить сообщение")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Длительность точки, мс.")
            TextField(
                value = dotDuration,
                onValueChange = onDotDurationChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MorseScreenPreview() {
    MorseTalkingTheme {
        MorseScreen(
            message = "Message",
            onMessageChange = {},
            onClick = {},
            dotDuration = "200",
            onDotDurationChange = {}
        )
    }
}