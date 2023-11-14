package com.example.morsetalking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.morsetalking.R
import com.example.morsetalking.ui.theme.MorseTalkingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseApp(
    viewModel: MorseScreenViewModel
) {
    MorseScreen(
        message = viewModel.userMessage,
        dotDuration = viewModel.dotDuration,
        sendSOS = { viewModel.sendSOS() },
        snackbarHostState = viewModel.snackbarHostState,
        onMessageChange = { viewModel.changeMessage(it) },
        sendMessage = { viewModel.sendUserMessage() },
        onDotDurationChange = { viewModel.changeDotDuration(it) },
        sendAudioMessage = viewModel.sendAudioMessage,
        changeAudioSending = { viewModel.changeAudioSending() },
        sendVisibleMessage = viewModel.sendVisibleMessage,
        changeVisibleSending = { viewModel.changeVisibleSending() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseScreen(
    message: String,
    dotDuration: String,
    sendSOS: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onMessageChange: (String) -> Unit,
    sendMessage: () -> Unit,
    onDotDurationChange: (String) -> Unit,
    sendAudioMessage: Boolean,
    changeAudioSending: (Boolean) -> Unit,
    sendVisibleMessage: Boolean,
    changeVisibleSending: (Boolean) -> Unit
) {
    val stringWithHyperText = buildAnnotatedString {
        val text = stringResource(R.string.background_information)
        val textWithLink = stringResource(R.string.text_with_link)
        val startIndex = text.indexOf(textWithLink)
        val endIndex = startIndex + textWithLink.length
        append(text)
        addStringAnnotation(
            "URL",
            stringResource(R.string.URL),
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
    Scaffold(
        floatingActionButton = { MorseFloatingActionBar(onClick = { sendSOS() }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(all = 10.dp)
        ) {
            TextField(
                value = message,
                onValueChange = onMessageChange,
                label = { Text(text = stringResource(R.string.enter_message_label)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Button(onClick = sendMessage) {
                Text(stringResource(R.string.button_send_message))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.dot_duration),
                    modifier = Modifier.weight(4f)
                )
                TextField(
                    value = dotDuration,
                    onValueChange = onDotDurationChange,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
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
                    text = stringResource(R.string.send_message_audio),
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
                    text = stringResource(R.string.send_message_visio),
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
}

@Composable
fun MorseFloatingActionBar(onClick: () -> Unit) {
    LargeFloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
    ) {
        Text(text = "SOS")
    }
}

@Preview(showSystemUi = true)
@Composable
fun MorseScreenPreview() {
    val snackbarHostState = SnackbarHostState()

    MorseTalkingTheme {
        MorseScreen(
            message = "Message",
            dotDuration = "100",
            sendSOS = {},
            snackbarHostState = snackbarHostState,
            onMessageChange = {},
            sendMessage = {},
            onDotDurationChange = {},
            sendAudioMessage = false,
            changeAudioSending = {},
            sendVisibleMessage = true,
            changeVisibleSending = {}
        )
    }
}