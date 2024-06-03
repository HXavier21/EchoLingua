package com.example.echolingua.ui.page.mainTranslatePages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.AudioTranslateTopBar
import com.example.echolingua.ui.component.MainPageLanguageSelector
import com.example.echolingua.ui.component.MicButton
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.page.audioTranscribePages.FaceToFaceChatPage
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun TranslateAudioPage(
    onBackClick: () -> Unit = {},
    onLanguageSelectClick: (SelectMode) -> Unit = {},
    onSwapLanguageClick: () -> Unit = {},
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    onRecordStart: () -> Unit = {},
    onRecordEnd: () -> Unit = {}
) {
    var isChatPageVisible by remember { mutableStateOf(false) }
    BackHandler {
        if (isChatPageVisible) {
            isChatPageVisible = false
        } else {
            onBackClick()
        }
    }
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            AudioTranslateTopBar(onBackClick = onBackClick, onChatClick = {
                isChatPageVisible = true
            })
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
                Column(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.extraLarge)
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = "Tap the mic button to start",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
            MainPageLanguageSelector(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                onSourceLanguageClick = { onLanguageSelectClick(SelectMode.SOURCE) },
                onTargetLanguageClick = { onLanguageSelectClick(SelectMode.TARGET) },
                onSwapLanguageClick = onSwapLanguageClick,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
            Row(modifier = Modifier.padding(vertical = 20.dp)) {
                MicButton(
                    modifier = Modifier
                        .scale(1.3f)
                        .weight(1f),
                    onRecordEnd = onRecordEnd,
                    onRecordStart = onRecordStart
                )
                MicButton(
                    modifier = Modifier
                        .scale(1.3f)
                        .weight(1f),
                    onRecordEnd = onRecordEnd,
                    onRecordStart = onRecordStart
                )
            }
        }
        if (isChatPageVisible) {
            FaceToFaceChatPage(onCloseClick = {
                isChatPageVisible = false
            })
        }
    }
}

@Preview
@Composable
fun TranslateAudioPagePreview() {
    EchoLinguaTheme {
        TranslateAudioPage()
    }
}