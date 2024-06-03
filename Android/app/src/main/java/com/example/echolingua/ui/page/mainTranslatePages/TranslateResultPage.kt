package com.example.echolingua.ui.page.mainTranslatePages

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.TranslateResultPageNavigationBar
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.ScrollDirection
import com.example.echolingua.util.rememberDirectionalLazyListState

@Composable
fun TranslateResultPage(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    sourceText: String = "",
    targetText: String = "",
    onShowPageChange: (PageState) -> Unit = {},
    onTTSClick: (String, String) -> Unit = { _, _ -> },
    onLanguageSelectClick: (SelectMode) -> Unit = {},
    onSourceTextClick: () -> Unit = {},
    setSourceText: (String) -> Unit = {},
    translateSourceText: (String) -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val surfaceContainerHighestColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val surfaceColor = MaterialTheme.colorScheme.surface
    val lazyListState = rememberLazyListState()
    val directionalLazyListState = rememberDirectionalLazyListState(
        lazyListState
    )


    LaunchedEffect(sourceText, sourceLanguage, targetLanguage) {
        translateSourceText(sourceText)
    }
    BackHandler {
        onShowPageChange(PageState.HOME_PAGE)
        setSourceText("")
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
    ) {
        Surface(
            modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                TranslateResultPageNavigationBar(onBackClick = {
                    onShowPageChange(PageState.HOME_PAGE)
                })
                LazyColumn(
                    state = lazyListState
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                ClickableText(
                                    text = AnnotatedString(sourceLanguage),
                                    modifier = Modifier.padding(start = 10.dp),
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    onLanguageSelectClick(SelectMode.SOURCE)
                                }
                                ClickableText(
                                    text = AnnotatedString(sourceText),
                                    style = textStyle.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 20.dp)
                                ) {
                                    onSourceTextClick()
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = {
                                        onTTSClick(
                                            sourceText, sourceLanguage
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                            contentDescription = "report"
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = {
                                        clipboardManager.setText(AnnotatedString(sourceText))
                                        Toast.makeText(
                                            context, "Text copied to clipboard", Toast.LENGTH_SHORT
                                        ).show()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.ContentCopy,
                                            contentDescription = "copy"
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(start = 100.dp, end = 100.dp)
                                .padding(vertical = 20.dp), color = Color(0xFF424758)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.padding(bottom = 100.dp)) {
                                ClickableText(
                                    text = AnnotatedString(targetLanguage),
                                    modifier = Modifier.padding(start = 10.dp),
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.surfaceTint,
                                    )
                                ) {
                                    onLanguageSelectClick(SelectMode.TARGET)
                                }
                                if (targetText.isNotEmpty()) {
                                    Text(
                                        text = targetText,
                                        style = textStyle.copy(
                                            color = MaterialTheme.colorScheme.surfaceTint
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 20.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = {
                                            onTTSClick(
                                                targetText, targetLanguage
                                            )
                                        }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                                contentDescription = "report",
                                                tint = MaterialTheme.colorScheme.surfaceTint
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        IconButton(onClick = {
                                            clipboardManager.setText(AnnotatedString(targetText))
                                            Toast.makeText(
                                                context,
                                                "Text copied to clipboard",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.ContentCopy,
                                                contentDescription = "copy",
                                                tint = MaterialTheme.colorScheme.surfaceTint
                                            )
                                        }
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Draw,
                                                contentDescription = "round_trip",
                                                tint = MaterialTheme.colorScheme.surfaceTint
                                            )
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = 10.dp, vertical = 20.dp
                                            )
                                            .width(200.dp)
                                            .height(30.dp)
                                            .clip(MaterialTheme.shapes.medium)
                                    ) {
                                        Canvas(modifier = Modifier.matchParentSize()) {
                                            val brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    surfaceContainerHighestColor.copy(0.6f),
                                                    surfaceColor
                                                )
                                            )
                                            drawRect(brush)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = {
                onShowPageChange(PageState.INPUT_PAGE)
                setSourceText("")
            },
            icon = { Icon(Icons.Filled.Add, "New Translation") },
            text = { Text(text = "New Translation") },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 40.dp),
            expanded = (directionalLazyListState.scrollDirection == ScrollDirection.Up)
        )

    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
fun TranslateResultPagePreview() {
    EchoLinguaTheme {
        TranslateResultPage(onShowPageChange = {})
    }
}