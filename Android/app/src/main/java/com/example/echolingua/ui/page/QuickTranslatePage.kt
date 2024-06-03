package com.example.echolingua.ui.page

import android.content.res.Configuration
import android.view.Gravity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolingua.QuickTranslateActivity
import com.example.echolingua.ui.component.ContentAfterTranslate
import com.example.echolingua.ui.component.ContentBeforeTranslate
import com.example.echolingua.ui.component.TransformMode
import com.example.echolingua.ui.component.QuickTranslatePageTopBar
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.ui.page.stateHolders.TranslateModelStateHolder
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.Translator
import kotlinx.coroutines.launch

enum class QuickTranslatePageState {
    BeforeTranslate, AfterTranslate
}

@Composable
fun QuickTranslatePageImpl(
    state: QuickTranslatePageState,
    initialText: String = "",
    translatedText: String = "",
    transformMode: TransformMode = TransformMode.Clear,
    onSourceLanguageClick: () -> Unit = {},
    onTargetLanguageClick: () -> Unit = {},
    onTTSClick: (String) -> Unit = {},
    onSourceTextClick: (String) -> Unit = {},
    onClearClick: (TransformMode) -> Unit = {},
    onTranslateClick: (String) -> Unit = {},
) {
    val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
    dialogWindowProvider.window.setGravity(Gravity.TOP)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .padding(top = 20.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            QuickTranslatePageTopBar()
            AnimatedContent(
                targetState = state, label = "AnimatedContent"
            ) { pageState ->
                if (pageState == QuickTranslatePageState.AfterTranslate) {
                    ContentAfterTranslate(sourceLanguage = LanguageSelectStateHolder.getSourceLanguageDisplayName(),
                        targetLanguage = LanguageSelectStateHolder.getTargetLanguageDisplayName(),
                        onSourceLanguageClick = { onSourceLanguageClick() },
                        onTargetLanguageClick = { onTargetLanguageClick() },
                        sourceText = initialText,
                        targetText = translatedText,
                        onTTSClick = { onTTSClick(it) },
                        onSourceTextClick = { onSourceTextClick(it) })
                } else {
                    ContentBeforeTranslate(initialText = initialText,
                        transformMode = transformMode,
                        sourceLanguage = LanguageSelectStateHolder.getSourceLanguageDisplayName(),
                        onTranslateClick = { onTranslateClick(it) },
                        onSourceLanguageClick = { onSourceLanguageClick() })
                }
            }
        }
        if (state == QuickTranslatePageState.AfterTranslate) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 13.dp)
            ) {
                Button(
                    onClick = {
                        onClearClick(TransformMode.Clear)
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        "Clear", color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onClearClick(TransformMode.NewTranslation)
                    }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceTint)
                ) {
                    Text(
                        "New translation", color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }

}

@Composable
fun QuickTranslatePage(
    initialText: String = "", quickTranslatePageViewModel: QuickTranslatePageViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    var state by remember { mutableStateOf(QuickTranslatePageState.AfterTranslate) }
    val sourceText by quickTranslatePageViewModel.sourceTextFlow.collectAsState()
    val translatedText by quickTranslatePageViewModel.targetTextFlow.collectAsState()
    val transformMode by quickTranslatePageViewModel.transformModeFlow.collectAsState()
    val sourceLanguage by LanguageSelectStateHolder.sourceLanguage
    val targetLanguage by LanguageSelectStateHolder.targetLanguage
    val isSelecting by LanguageSelectStateHolder.isSelecting

    LaunchedEffect(sourceLanguage, targetLanguage) {
        quickTranslatePageViewModel.setSourceText(initialText)
        quickTranslatePageViewModel.setTargetText("")
        Translator.translateWithAutoDetect(initialText, onSuccessCallback = {
            quickTranslatePageViewModel.setTargetText(it)
        })
    }
    Dialog(
        onDismissRequest = { (context as QuickTranslateActivity).finish() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box {
            QuickTranslatePageImpl(state = state,
                initialText = sourceText,
                translatedText = translatedText,
                transformMode = transformMode,
                onSourceLanguageClick = {
                    LanguageSelectStateHolder.navigateToLanguageSelectPage(
                        SelectMode.SOURCE
                    )
                },
                onTargetLanguageClick = {
                    LanguageSelectStateHolder.navigateToLanguageSelectPage(
                        SelectMode.TARGET
                    )
                },
                onSourceTextClick = {
                    quickTranslatePageViewModel.setTransformMode(TransformMode.NewTranslation)
                    state = QuickTranslatePageState.BeforeTranslate
                },
                onClearClick = {
                    with(quickTranslatePageViewModel) {
                        setSourceText("")
                        setTargetText("")
                        setTransformMode(it)
                    }
                    state = QuickTranslatePageState.BeforeTranslate
                },
                onTranslateClick = { text ->
                    quickTranslatePageViewModel.setSourceText(text)
                    coroutine.launch {
                        Translator.translateWithAutoDetect(text, onSuccessCallback = {
                            quickTranslatePageViewModel.setTargetText(it)
                        })
                    }
                    state = QuickTranslatePageState.AfterTranslate
                })
            AnimatedVisibility(
                visible = isSelecting, enter = fadeIn() + scaleIn(
                    initialScale = 0.9f
                ), exit = fadeOut()
            ) {
                LanguageSelectPage(selectMode = LanguageSelectStateHolder.selectMode.value,
                    onBackClick = { LanguageSelectStateHolder.isSelecting.value = false },
                    onModelStateClick = {
                        TranslateModelStateHolder.currentLanguage.value = it
                        TranslateModelStateHolder.isManagingModel.value = true
                    })
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun QuickTranslatePagePreview() {
    EchoLinguaTheme {
        QuickTranslatePageImpl(state = QuickTranslatePageState.AfterTranslate)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun DarkQuickTranslatePagePreview() {
    EchoLinguaTheme {
        QuickTranslatePageImpl(state = QuickTranslatePageState.BeforeTranslate)
    }
}


