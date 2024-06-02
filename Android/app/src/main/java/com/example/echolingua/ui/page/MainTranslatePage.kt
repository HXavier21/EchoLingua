package com.example.echolingua.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

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
    currentPage: PageState = PageState.HOME_PAGE
) {
    var showPage by remember { mutableStateOf(currentPage) }
    val sourceText by mainTranslatePageViewModel.sourceTextFlow.collectAsState()
    val targetText by mainTranslatePageViewModel.targetTextFlow.collectAsState()
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
                onLanguageSelectClick = {})
        }

        PageState.INPUT_PAGE -> {
            TranslateInputPage(sourceText = sourceText, onShowPageChange = {
                showPage = it
            }, pasteText = {
                mainTranslatePageViewModel.setSourceText(it)
            }, setSourceText = {
                mainTranslatePageViewModel.setSourceText(it)
            })
        }

        PageState.DISPLAY_PAGE -> {
            TranslateResultPage(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                sourceText = sourceText,
                targetText = targetText,
                onShowPageChange = {
                    showPage = it
                })
        }
    }
}