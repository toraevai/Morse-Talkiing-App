package com.example.morsetalking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.morsetalking.ui.theme.MorseTalkingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseApp(
    viewModel: MorseScreenViewModel
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) }
    ) { contentPadding ->
        MorseScreen(
            message = viewModel.message,
            dotDuration = viewModel.dotDuration,
            onMessageChange = { viewModel.changeMessage(it) },
            sendMessage = { viewModel.sendMessage() },
            onDotDurationChange = { viewModel.changeDotDuration(it) },
            sendAudioMessage = viewModel.sendAudioMessage,
            changeAudioSending = { viewModel.changeAudioSending() },
            sendVisibleMessage = viewModel.sendVisibleMessage,
            changeVisibleSending = { viewModel.changeVisibleSending() },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseScreen(
    message: String,
    dotDuration: String,
    onMessageChange: (String) -> Unit,
    sendMessage: () -> Unit,
    onDotDurationChange: (String) -> Unit,
    sendAudioMessage: Boolean,
    changeAudioSending: (Boolean) -> Unit,
    sendVisibleMessage: Boolean,
    changeVisibleSending: (Boolean) -> Unit,
    modifier: Modifier
) {
    val stringWithHyperText = buildAnnotatedString {
        val text = "\tВ соответствии с Википедией, в стандартном коде Морзе за единицу времени " +
                "принимается длительность самого короткого сигнала — точки. Длительность тире " +
                "равна трём точкам. Пауза между элементами одного знака — одна точка, между " +
                "знаками в слове — 3 точки, между словами — 7 точек."
        val textWithLink = "Википедией"
        val startIndex = text.indexOf(textWithLink)
        val endIndex = startIndex + textWithLink.length
        append(text)
        addStringAnnotation(
            "URL",
            "https://ru.wikipedia.org/wiki/Азбука_Морзе",
            start = startIndex,
            end = endIndex
        )
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ),
            start = startIndex,
            end = endIndex
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(all = 10.dp)
    ) {
        TextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text(text = "Введите сообщение") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(onClick = sendMessage) {
            Text("Отправить сообщение")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Длительность точки, мс.",
                modifier = Modifier.weight(4f)
            )
            TextField(
                value = dotDuration,
                onValueChange = onDotDurationChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Отправить сообщение по звуковому каналу",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = sendAudioMessage,
                onCheckedChange = changeAudioSending
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Отправить сообщение по видимому каналу",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = sendVisibleMessage,
                onCheckedChange = changeVisibleSending
            )
        }

        val uriHandler = LocalUriHandler.current

        ClickableText(
            text = stringWithHyperText,
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Justify),
            onClick = {
                stringWithHyperText
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        uriHandler.openUri(stringAnnotation.item)
                    }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MorseScreenPreview() {
    MorseTalkingTheme {
        MorseScreen(
            message = "Message",
            onMessageChange = {},
            sendMessage = {},
            dotDuration = "100",
            onDotDurationChange = {},
            sendAudioMessage = false,
            changeAudioSending = {},
            sendVisibleMessage = true,
            changeVisibleSending = {},
            modifier = Modifier
        )
    }
}