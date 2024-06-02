package com.example.echolingua.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.MainPageLanguageSelector
import com.example.echolingua.ui.component.TranslateInputTextField
import com.example.echolingua.ui.component.TranslateInputNavigationBar
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun TranslateInputPage(
    modifier: Modifier = Modifier,
    onShowPageChange: (PageState) -> Unit,
    sourceText: String = "",
    pasteText: (String) -> Unit = {},
    setSourceText: (String) -> Unit = {}
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            val focusRequester = remember { FocusRequester() }
            BackHandler {
                onShowPageChange(PageState.HOME_PAGE)
                setSourceText("")
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
                TranslateInputTextField(modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                    initialText = sourceText,
                    pasteText = { pasteText(it) },
                    onEnterPressed = {
                        onShowPageChange(PageState.DISPLAY_PAGE)
                        setSourceText(it)
                    })
                MainPageLanguageSelector(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(vertical = 10.dp),
                    textPadding = 0.dp,
                    shape = MaterialTheme.shapes.extraLarge
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