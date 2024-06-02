package com.example.echolingua.ui.page.mainTranslatePages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.MainPageLanguageSelector
import com.example.echolingua.ui.component.MainPageNavigationBar
import com.example.echolingua.ui.component.MainTranslatePageBottomBar
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.TextProcessUtil

@Composable
fun TranslateHomePage(
    modifier: Modifier = Modifier,
    onNavigateToDataPage: () -> Unit = {},
    onShowPageChange: () -> Unit = {},
    onLanguageSelectClick: (SelectMode) -> Unit = {},
    onSwapLanguageClick: () -> Unit = {},
    onNavigateToAudioTranscribePage: () -> Unit = {},
    onNavigateToCameraPage: () -> Unit = {},
    onMicClick: () -> Unit = {},
    pasteText: (String) -> Unit = {},
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target"
) {
    val clipboardManager = LocalClipboardManager.current
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            MainPageNavigationBar(onNavigateToDataPage = onNavigateToDataPage)
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
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, // 设置interactionSource
                            indication = null, onClick = {
                                onShowPageChange()
                            })
                ) {
                    TextField(
                        value = "",
                        onValueChange = { },
                        enabled = false,
                        placeholder = {
                            Text(
                                "Enter text",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        textStyle = MaterialTheme.typography.headlineLarge,
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            disabledIndicatorColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            clipboardManager.getText()?.let {
                                pasteText(it.text)
                            }
                        },
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(20.dp),
                        contentPadding = PaddingValues(horizontal = 15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentPaste,
                            contentDescription = "Paste",
                            modifier = Modifier.scale(0.8f)
                        )
                        Text(text = "Paste", modifier = Modifier.padding(horizontal = 6.dp))
                    }
                }
            }
            MainPageLanguageSelector(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
                onSourceLanguageClick = { onLanguageSelectClick(SelectMode.SOURCE) },
                onTargetLanguageClick = { onLanguageSelectClick(SelectMode.TARGET) },
                onSwapLanguageClick = onSwapLanguageClick,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
            MainTranslatePageBottomBar(
                modifier = Modifier.padding(vertical = 24.dp),
                onNavigateToChatPage = onNavigateToAudioTranscribePage,
                onMicClick = onMicClick,
                onNavigateToCameraPage = onNavigateToCameraPage
            )
        }
    }
}

@Composable
@Preview
fun TranslateHomePagePreview() {
    EchoLinguaTheme {
        TranslateHomePage()
    }
}