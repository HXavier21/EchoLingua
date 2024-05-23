package com.example.echolingua.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TranslatePage(
    translatePageViewModel: TranslatePageViewModel = viewModel(),
    onNavigateToCameraTranslatePage: () -> Unit = {},
    onNavigateToAudioTranscribePage: () -> Unit = {},
    onNavigateToLanguageSelectPage: (SelectMode) -> Unit = {},
    onNavigateToBackEndTestPage: () -> Unit = {}
) {
    val translatedText by translatePageViewModel.translatedTextFlow.collectAsState()
    val focusManager = LocalFocusManager.current
    var text by remember {
        mutableStateOf("")
    }
    Card(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = LanguageSelectStateHolder.getSourceLanguageDisplayName()
                        )
                    },
                    onClick = {
                        focusManager.clearFocus()
                        onNavigateToLanguageSelectPage(SelectMode.SOURCE)
                    }
                )
            }
            Icon(
                imageVector = Icons.Default.ImportExport,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(rotationZ = 90f)
                    .clickable {
                        LanguageSelectStateHolder.swapLanguage()
                    }
            )
            Box(modifier = Modifier.weight(1f)) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = LanguageSelectStateHolder.getTargetLanguageDisplayName()
                        )
                    },
                    onClick = {
                        focusManager.clearFocus()
                        onNavigateToLanguageSelectPage(SelectMode.TARGET)
                    }
                )
            }
        }
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        translatePageViewModel.translate(text)
                    }
                )
            }
        )
        Text(text = translatedText, modifier = Modifier.padding(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onNavigateToAudioTranscribePage,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Audio Translate")
            }
            Button(
                onClick = onNavigateToCameraTranslatePage,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Camera Translate")
            }
        }
        Button(
            onClick = { onNavigateToBackEndTestPage() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "To BackEndTestPage")
        }
    }
}

@Preview
@Composable
fun TranslatePagePreview() {
    TranslatePage()
}