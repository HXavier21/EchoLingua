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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.AudioTranslateResult
import com.example.echolingua.ui.component.AudioTranslateTopBar
import com.example.echolingua.ui.component.MainPageLanguageSelector
import com.example.echolingua.ui.component.MicButton
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.page.audioTranscribePages.FaceToFaceChatPage
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.Recorder
import com.example.echolingua.util.Translator
import kotlinx.coroutines.launch

enum class TranscribeMode {
    SOURCE2TARGET, TARGET2SOURCE
}

@Composable
fun TranslateAudioPage(
    onBackClick: () -> Unit = {},
    onLanguageSelectClick: (SelectMode) -> Unit = {},
    onSwapLanguageClick: () -> Unit = {},
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    onRecordStart: () -> Unit = {},
    onRecordEnd: (String, (String) -> Unit) -> Unit = { _, _ -> },
    sourceText: String = "",
    targetText: String = "",
    setTargetText: (String) -> Unit = {},
    setSourceText: (String) -> Unit = {},
    onDetailsClick: () -> Unit = {},
    updateStyleCallback: (TextStyle) -> Unit = {}
) {
    val recorderState by Recorder.recordState
    var isChatPageVisible by remember { mutableStateOf(false) }
    var clickedButtonIndex by remember { mutableIntStateOf(-1) }
    var transcribeMode by remember { mutableStateOf(TranscribeMode.SOURCE2TARGET) }
    val coroutine = rememberCoroutineScope()
    val sourceLanguageKey = LanguageSelectStateHolder.getKeyByDisplayName(sourceLanguage)
    val targetLanguageKey = LanguageSelectStateHolder.getKeyByDisplayName(targetLanguage)
    LaunchedEffect(sourceLanguage, targetLanguage) {
        when (transcribeMode) {
            TranscribeMode.SOURCE2TARGET -> {
                Translator.translateWithAutoDetect(
                    sourceText,
                    onSuccessCallback = { result -> setTargetText(result) },
                    sourceLanguage = sourceLanguageKey,
                    targetLanguage = targetLanguageKey
                )
            }

            TranscribeMode.TARGET2SOURCE -> {
                Translator.translateWithAutoDetect(
                    targetText,
                    onSuccessCallback = { result -> setSourceText(result) },
                    sourceLanguage = targetLanguageKey,
                    targetLanguage = sourceLanguageKey
                )
            }
        }
    }
    BackHandler {
        if (isChatPageVisible) {
            isChatPageVisible = false
        } else {
            coroutine.launch {
                Recorder.cancelRecording()
                onBackClick()
            }
        }
    }
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            AudioTranslateTopBar(onBackClick = {
                coroutine.launch {
                    Recorder.cancelRecording()
                    onBackClick()
                }
            }, onChatClick = {
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
                    if (sourceText.isEmpty() && targetText.isEmpty()) {
                        Text(
                            text = when (recorderState) {
                                Recorder.RecorderState.RECORDING -> "Recording..."
                                Recorder.RecorderState.TRANSCRIBING -> "Transcribing..."
                                else -> "Tap the mic button to start"
                            },
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(20.dp)
                        )
                    } else {
                        when (transcribeMode) {
                            TranscribeMode.SOURCE2TARGET -> AudioTranslateResult(
                                sourceText = sourceText,
                                targetText = targetText,
                                sourceLanguage = sourceLanguage,
                                targetLanguage = targetLanguage,
                                onTTSClick = {},
                                onDetailsClick = onDetailsClick,
                                updateStyleCallback = updateStyleCallback
                            )

                            TranscribeMode.TARGET2SOURCE -> AudioTranslateResult(
                                sourceText = targetText,
                                targetText = sourceText,
                                sourceLanguage = targetLanguage,
                                targetLanguage = sourceLanguage,
                                onTTSClick = {},
                                onDetailsClick = onDetailsClick,
                                updateStyleCallback = updateStyleCallback
                            )
                        }
                    }
                }
            }
            MainPageLanguageSelector(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                onSourceLanguageClick = { onLanguageSelectClick(SelectMode.SOURCE) },
                onTargetLanguageClick = { onLanguageSelectClick(SelectMode.TARGET) },
                onSwapLanguageClick = onSwapLanguageClick,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                enabled = recorderState == Recorder.RecorderState.IDLE
            )
            Row(modifier = Modifier.padding(vertical = 20.dp)) {
                MicButton(modifier = Modifier
                    .scale(1.3f)
                    .weight(1f),
                    onRecordEnd = {
                        onRecordEnd(sourceLanguage) {
                            setSourceText(it)
                            coroutine.launch {
                                Translator.translateWithAutoDetect(
                                    it,
                                    onSuccessCallback = { result -> setTargetText(result) },
                                    sourceLanguage = sourceLanguageKey,
                                    targetLanguage = targetLanguageKey
                                )
                            }
                        }
                        transcribeMode = TranscribeMode.SOURCE2TARGET
                        clickedButtonIndex = -1
                    },
                    onRecordStart = {
                        onRecordStart()
                        clickedButtonIndex = 1
                    },
                    recordState = if (clickedButtonIndex != 0) recorderState else Recorder.RecorderState.UNAVAILABLE,
                    onUnavailableClick = {
                        coroutine.launch {
                            Recorder.cancelRecording()
                            clickedButtonIndex = 1
                            onRecordStart()
                        }
                    })
                MicButton(modifier = Modifier
                    .scale(1.3f)
                    .weight(1f),
                    onRecordEnd = {
                        onRecordEnd(targetLanguage) {
                            setTargetText(it)
                            coroutine.launch {
                                Translator.translateWithAutoDetect(
                                    it,
                                    onSuccessCallback = { result -> setSourceText(result) },
                                    sourceLanguage = targetLanguageKey,
                                    targetLanguage = sourceLanguageKey
                                )
                            }
                        }
                        transcribeMode = TranscribeMode.TARGET2SOURCE
                        clickedButtonIndex = -1

                    },
                    onRecordStart = {
                        onRecordStart()
                        clickedButtonIndex = 0
                    },
                    recordState = if (clickedButtonIndex != 1) recorderState else Recorder.RecorderState.UNAVAILABLE,
                    onUnavailableClick = {
                        coroutine.launch {
                            Recorder.cancelRecording()
                            clickedButtonIndex = 0
                            onRecordStart()
                        }
                    })
            }
        }
    }
    if (isChatPageVisible) {
        FaceToFaceChatPage(
            onCloseClick = {
                isChatPageVisible = false
            },
            clickedButtonIndex = clickedButtonIndex,
            recorderState = recorderState,
            onRecordEnd = { language, callback ->
                onRecordEnd(language) {
                    callback(it)
                }
                clickedButtonIndex = -1
            },
            onRecordStart = {
                onRecordStart()
                clickedButtonIndex = it
            },
            onUnavailableClick = {
                coroutine.launch {
                    Recorder.cancelRecording()
                    clickedButtonIndex = it
                    onRecordStart()
                }
            },
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            setTargetText = setTargetText,
            setSourceText = setSourceText,
            sourceText = sourceText,
            targetText = targetText
        )
    }
}

@Preview
@Composable
fun TranslateAudioPagePreview() {
    EchoLinguaTheme {
        TranslateAudioPage()
    }
}