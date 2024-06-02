package com.example.echolingua.ui.page.mainTranslatePages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.util.Translator
import kotlinx.coroutines.launch

private const val TAG = "MainTranslatePage"

// 定义页面状态枚举
enum class PageState {
    HOME_PAGE, INPUT_PAGE, DISPLAY_PAGE
}

@Composable
fun MainTranslatePage(
    modifier: Modifier = Modifier,
    mainTranslatePageViewModel: MainTranslatePageViewModel = viewModel(),
    onNavigateToDataPage: () -> Unit = {},
    currentPage: PageState = PageState.HOME_PAGE,
    onLanguageSelectClick: (SelectMode) -> Unit = {},
    onNavigateToAudioTranscribePage: () -> Unit = {},
    onNavigateToCameraPage: () -> Unit = {},
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
                pasteText = {
                    mainTranslatePageViewModel.setSourceText(it)
                    showPage = PageState.INPUT_PAGE
                },
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                onSwapLanguageClick = {
                    LanguageSelectStateHolder.swapLanguage()
                },
                onLanguageSelectClick = onLanguageSelectClick,
                onNavigateToAudioTranscribePage = onNavigateToAudioTranscribePage,
                onNavigateToCameraPage = onNavigateToCameraPage
            )
        }

        PageState.INPUT_PAGE -> {
            TranslateInputPage(sourceText = sourceText,
                onShowPageChange = {
                    showPage = it
                },
                pasteText = {
                    mainTranslatePageViewModel.setSourceText(it)
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
                    showPage = it
                },
                setSourceText = {
                    mainTranslatePageViewModel.setSourceText(it)
                },
                onLanguageSelectClick = onLanguageSelectClick,
                onSourceTextClick = {
                    showPage = PageState.INPUT_PAGE
                },
                onTTSClick = {},
                translateSourceText = {
                    mainTranslatePageViewModel.setTargetText("")
                    coroutine.launch {
                        Translator.translateWithAutoDetect(it, onSuccessCallback = { text ->
                            mainTranslatePageViewModel.setTargetText(text)
                        })
                    }
                })
        }
    }
}