package com.example.morsetalking.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.morsetalking.ui.theme.MorseTalkingTheme

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MorseApp(viewModel: MorseScreenViewModel) {
    val context = LocalContext.current
    MorseScreen(
        message = viewModel.message,
        dotDuration = viewModel.dotDuration,
        onMessageChange = { viewModel.message = it },
        onClick = { viewModel.sendMessage() },
        onDotDurationChange = { viewModel.changeDotDuration(it.toLong(), context) }
    )
}

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseScreen(
    message: String,
    dotDuration: Long,
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
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Длительность точки, мс.")
            TextField(
                value = dotDuration.toString(),
                onValueChange = onDotDurationChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }*/
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Preview(showSystemUi = true)
@Composable
fun MorseScreenPreview() {
    MorseTalkingTheme {
        MorseScreen(
            message = "Message",
            onMessageChange = {},
            onClick = {},
            dotDuration = 200,
            onDotDurationChange = {}
        )
    }
}