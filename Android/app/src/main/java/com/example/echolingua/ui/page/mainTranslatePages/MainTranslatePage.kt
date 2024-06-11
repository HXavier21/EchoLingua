package com.example.echolingua.ui.page.mainTranslatePages

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import com.example.echolingua.App
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.ui.page.stateHolders.WhisperModelStateHolder
import com.example.echolingua.util.OnlineServiceUtil
import com.example.echolingua.util.Recorder
import com.example.echolingua.util.Translator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "MainTranslatePage"

// 定义页面状态枚举
enum class PageState {
    HOME_PAGE, INPUT_PAGE, DISPLAY_PAGE, AUDIO_PAGE
}

@Composable
fun MainTranslatePage(
    modifier: Modifier = Modifier,
    mainTranslatePageViewModel: MainTranslatePageViewModel = viewModel(),
    onNavigateToDataPage: () -> Unit = {},
    currentPage: PageState = PageState.HOME_PAGE,
    onLanguageSelectClick: (SelectMode) -> Unit = {},
    onNavigateToCameraPage: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    val coroutine = rememberCoroutineScope()
    var showPage by remember { mutableStateOf(currentPage) }
    val sourceText by mainTranslatePageViewModel.sourceTextFlow.collectAsState()
    val targetText by mainTranslatePageViewModel.targetTextFlow.collectAsState()
    val textStyle by mainTranslatePageViewModel.textStyleFlow.collectAsState()
    val sourceLanguage = LanguageSelectStateHolder.getSourceLanguageDisplayName()
    val targetLanguage = LanguageSelectStateHolder.getTargetLanguageDisplayName()
    when (showPage) {
        PageState.HOME_PAGE -> {
            TranslateHomePage(onShowPageChange = {
                showPage = PageState.INPUT_PAGE
            },
                onNavigateToDataPage = onNavigateToDataPage,
                setSourceText = {
                    mainTranslatePageViewModel.setSourceText(it)
                },
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                onSwapLanguageClick = {
                    LanguageSelectStateHolder.swapLanguage()
                },
                onLanguageSelectClick = onLanguageSelectClick,
                onNavigateToAudioTranscribePage = {
                    showPage = PageState.AUDIO_PAGE
                },
                onNavigateToCameraPage = onNavigateToCameraPage,
                onRecordStart = {
                    if (WhisperModelStateHolder.canTranscribe.value) {
                        coroutine.launch {
                            Recorder.startRecording()
                        }
                    }
                },
                onRecordEnd = {
                    coroutine.launch {
                        Recorder.stopRecording(language = sourceLanguage) {
                            mainTranslatePageViewModel.setSourceText(it)
                        }
                        showPage = PageState.INPUT_PAGE
                    }

                },
                onRecordCancel = {
                    coroutine.launch {
                        Recorder.cancelRecording()
                    }
                },
                onSettingClick = onSettingClick
            )
        }

        PageState.INPUT_PAGE -> {
            TranslateInputPage(sourceText = sourceText,
                onShowPageChange = {
                    showPage = it
                },
                setSourceText = {
                    mainTranslatePageViewModel.setSourceText(it)
                },
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                onSwapLanguageClick = {
                    LanguageSelectStateHolder.swapLanguage()
                },
                onLanguageSelectClick = onLanguageSelectClick,
                updateStyleCallback = {
                    mainTranslatePageViewModel.setTextStyle(it)
                })
        }

        PageState.DISPLAY_PAGE -> {
            TranslateResultPage(sourceLanguage = sourceLanguage,
                textStyle = textStyle,
                targetLanguage = targetLanguage,
                sourceText = sourceText,
                targetText = targetText,
                onShowPageChange = {
                    mainTranslatePageViewModel.setSourceText("")
                    mainTranslatePageViewModel.setTargetText("")
                    showPage = it
                },
                onLanguageSelectClick = onLanguageSelectClick,
                onSourceTextClick = {
                    showPage = PageState.INPUT_PAGE
                },
                onTTSClick = { text, language ->
                    coroutine.launch {
                        OnlineServiceUtil.getTTSService(text = text,
                            language = language,
                            onResponseCallback = { response ->
                                coroutine.launch {
                                    val file = File.createTempFile("tempAudio", ".wav")
                                    withContext(Dispatchers.IO) {
                                        response.body?.bytes().let {
                                            file.apply {
                                                writeBytes(it ?: byteArrayOf())
                                                Log.d(
                                                    TAG, "onResponse: file copied"
                                                )
                                            }
                                        }
                                    }
                                    withContext(Dispatchers.Main) {
                                        App.player.apply {
                                            setMediaItem(
                                                MediaItem.fromUri(
                                                    file.toUri()
                                                )
                                            )
                                            prepare()
                                            play()
                                            Log.d(
                                                TAG, "onResponse: play over"
                                            )
                                        }
                                    }
                                }
                            },
                            onFailureCallback = {
                                coroutine.launch {
                                    withContext(Dispatchers.Main) {
                                        Log.e(TAG, "MainTranslatePage: ", it)
                                        Toast.makeText(
                                            App.context, "TTS failed", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                    }
                },
                translateSourceText = {
                    mainTranslatePageViewModel.setTargetText("")
                    coroutine.launch {
                        Translator.translateWithAutoDetect(it, onSuccessCallback = { text ->
                            mainTranslatePageViewModel.setTargetText(text)
                        })
                    }
                })
        }

        PageState.AUDIO_PAGE -> {
            TranslateAudioPage(onBackClick = {
                mainTranslatePageViewModel.setSourceText("")
                mainTranslatePageViewModel.setTargetText("")
                showPage = PageState.HOME_PAGE
            },
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                onSwapLanguageClick = {
                    LanguageSelectStateHolder.swapLanguage()
                },
                onLanguageSelectClick = onLanguageSelectClick,
                onRecordStart = {
                    mainTranslatePageViewModel.setSourceText("")
                    mainTranslatePageViewModel.setTargetText("")
                    if (WhisperModelStateHolder.canTranscribe.value) {
                        coroutine.launch {
                            Recorder.startRecording()
                        }
                    }
                },
                onRecordEnd = { language, callback ->
                    coroutine.launch {
                        Recorder.stopRecording(language) {
                            callback(it)
                        }
                    }
                },
                sourceText = sourceText,
                targetText = targetText,
                setTargetText = {
                    mainTranslatePageViewModel.setTargetText(it)
                },
                setSourceText = {
                    mainTranslatePageViewModel.setSourceText(it)
                },
                updateStyleCallback = {
                    mainTranslatePageViewModel.setTextStyle(it)
                },
                onDetailsClick = {
                    showPage = PageState.DISPLAY_PAGE

                })
        }
    }
}