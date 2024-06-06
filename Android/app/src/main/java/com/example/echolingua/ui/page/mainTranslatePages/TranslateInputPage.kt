package com.example.echolingua.ui.page.mainTranslatePages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.MainPageLanguageSelector
import com.example.echolingua.ui.component.TranslateInputNavigationBar
import com.example.echolingua.ui.component.TranslateInputTextField
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun TranslateInputPage(
    modifier: Modifier = Modifier,
    onShowPageChange: (PageState) -> Unit,
    sourceText: String = "",
    updateStyleCallback: (TextStyle) -> Unit = {},
    pasteText: (String) -> Unit = {},
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    onLanguageSelectClick: (SelectMode) -> Unit = {},
    onSwapLanguageClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            BackHandler {
                onShowPageChange(PageState.HOME_PAGE)
            }
            TranslateInputNavigationBar(onBackClick = {
                onShowPageChange(PageState.HOME_PAGE)
            })
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                TranslateInputTextField(
                    modifier = Modifier.weight(1f),
                    initialText = sourceText,
                    pasteText = { pasteText(it) },
                    onEnterPressed = {
                        pasteText(it)
                        onShowPageChange(PageState.DISPLAY_PAGE)
                    },
                    updateStyleCallback = updateStyleCallback
                )
                MainPageLanguageSelector(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(vertical = 10.dp),
                    textPadding = 0.dp,
                    shape = MaterialTheme.shapes.extraLarge,
                    onSourceLanguageClick = {
                        onLanguageSelectClick(SelectMode.SOURCE)
                        focusManager.clearFocus()
                    },
                    onTargetLanguageClick = {
                        onLanguageSelectClick(SelectMode.TARGET)
                        focusManager.clearFocus()
                    },
                    onSwapLanguageClick = onSwapLanguageClick,
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage
                )
            }
        }
    }
}

@Composable
@Preview
fun TranslateInputPagePreview() {
    EchoLinguaTheme {
        TranslateInputPage(onShowPageChange = {})
    }
}