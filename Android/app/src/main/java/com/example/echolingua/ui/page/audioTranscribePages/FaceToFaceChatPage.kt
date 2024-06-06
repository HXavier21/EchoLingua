package com.example.echolingua.ui.page.audioTranscribePages

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.FaceToFaceChatItem
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.Recorder
import com.example.echolingua.util.Translator
import kotlinx.coroutines.launch

@Composable
fun FaceToFaceChatPage(
    onCloseClick: () -> Unit = {},
    clickedButtonIndex: Int = -1,
    recorderState: Recorder.RecorderState = Recorder.RecorderState.IDLE,
    onRecordStart: (Int) -> Unit = {},
    onRecordEnd: (String, (String) -> Unit) -> Unit = { _, _ -> },
    onUnavailableClick: (Int) -> Unit = {},
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    setTargetText: (String) -> Unit = {},
    setSourceText: (String) -> Unit = {},
    sourceText: String = "",
    targetText: String = ""
) {
    val coroutine = rememberCoroutineScope()
    val sourceLanguageKey = LanguageSelectStateHolder.getKeyByDisplayName(sourceLanguage)
    val targetLanguageKey = LanguageSelectStateHolder.getKeyByDisplayName(targetLanguage)
    var initialSourceText by remember { mutableStateOf("") }
    var initialTargetText by remember { mutableStateOf("") }
    BackHandler {
        onCloseClick()
    }
    LaunchedEffect(Unit) {
        Translator.translateWithAutoDetect(
            "Tap the microphone button to start",
            onSuccessCallback = { initialSourceText = it },
            sourceLanguage = "en",
            targetLanguage = sourceLanguageKey
        )
        Translator.translateWithAutoDetect(
            "Tap the microphone button to start",
            onSuccessCallback = { initialTargetText = it },
            sourceLanguage = "en",
            targetLanguage = targetLanguageKey
        )
    }
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {
            FaceToFaceChatItem(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .graphicsLayer(rotationZ = 180f),
                language = targetLanguage,
                text = targetText.ifEmpty { initialTargetText },
                recorderState = if (clickedButtonIndex != 0) recorderState else Recorder.RecorderState.UNAVAILABLE,
                onRecordStart = { onRecordStart(1) },
                onRecordEnd = {
                    onRecordEnd(it) { text ->
                        coroutine.launch {
                            setTargetText(text)
                            Translator.translateWithAutoDetect(
                                text,
                                onSuccessCallback = { result -> setSourceText(result) },
                                sourceLanguage = targetLanguageKey,
                                targetLanguage = sourceLanguageKey
                            )
                        }
                    }
                },
                onUnavailableClick = { onUnavailableClick(1) })
            IconButton(modifier = Modifier.padding(horizontal = 12.dp),
                onClick = { onCloseClick() }) {
                Icon(
                    imageVector = Icons.Default.Close, contentDescription = "Close"

                )
            }
            FaceToFaceChatItem(modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                language = sourceLanguage,
                text = sourceText.ifEmpty { initialSourceText },
                recorderState = if (clickedButtonIndex != 1) recorderState else Recorder.RecorderState.UNAVAILABLE,
                onRecordStart = { onRecordStart(0) },
                onRecordEnd = {
                    onRecordEnd(it) { text ->
                        coroutine.launch {
                            setSourceText(text)
                            Translator.translateWithAutoDetect(
                                text,
                                onSuccessCallback = { result -> setTargetText(result) },
                                sourceLanguage = sourceLanguageKey,
                                targetLanguage = targetLanguageKey
                            )
                        }
                    }
                },
                onUnavailableClick = { onUnavailableClick(0) })
        }
    }

}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
fun FaceToFaceChatPagePreview() {
    EchoLinguaTheme {
        FaceToFaceChatPage()
    }
}