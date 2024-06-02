package com.example.echolingua.ui.page

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echolingua.ui.component.TranslateResultPageNavigationBar
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun TranslateResultPage(
    modifier: Modifier = Modifier,
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    sourceText: String = "",
    targetText: String = "",
    onShowPageChange: (PageState) -> Unit = {},
    onTTSClick: (String) -> Unit = {},
    onSourceLanguageClick: () -> Unit = {},
    onTargetLanguageClick: () -> Unit = {},
    onSourceTextClick: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current
    val surfaceContainerHighestColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val surfaceColor = MaterialTheme.colorScheme.surface
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            BackHandler {
                onShowPageChange(PageState.HOME_PAGE)
            }
            Scaffold(topBar = {
                TranslateResultPageNavigationBar(onBackClick = {
                    onShowPageChange(PageState.HOME_PAGE)
                })
            }, bottomBar = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    ExtendedFloatingActionButton(
                        onClick = { onShowPageChange(PageState.INPUT_PAGE) },
                        icon = { Icon(Icons.Filled.Add, "New Translation") },
                        text = { Text(text = "New Translation") },
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(vertical = 35.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }) {
                Surface(
                    modifier = modifier.padding(it), color = MaterialTheme.colorScheme.surface
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                    onSourceLanguageClick()
                                }
                                ClickableText(
                                    text = AnnotatedString(sourceText),
                                    style = MaterialTheme.typography.headlineLarge.copy(
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
                                    IconButton(onClick = { onTTSClick(sourceText) }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                            contentDescription = "report"
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = {
                                        clipboardManager.setText(AnnotatedString(sourceText))
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
                            modifier = Modifier.padding(start = 100.dp, end = 100.dp),
                            color = Color(0xFF424758)
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                ClickableText(
                                    text = AnnotatedString(targetLanguage),
                                    modifier = Modifier.padding(start = 10.dp),
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.surfaceTint,
                                    )
                                ) {
                                    onTargetLanguageClick()
                                }
                                if (targetText.isNotEmpty()) {
                                    Text(
                                        text = targetText,
                                        style = MaterialTheme.typography.headlineLarge,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 20.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { onTTSClick(targetText) }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                                contentDescription = "report"
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        IconButton(onClick = {
                                            clipboardManager.setText(AnnotatedString(targetText))
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.ContentCopy,
                                                contentDescription = "copy"
                                            )
                                        }
                                        IconButton(onClick = { /*TODO*/ }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Draw,
                                                contentDescription = "round_trip"
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