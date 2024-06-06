package com.example.echolingua.ui.page.quickTranslatePages

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme

@Composable
fun ContentAfterTranslate(
    sourceLanguage: String = "Auto detect",
    targetLanguage: String = "English",
    onSourceLanguageClick: () -> Unit = {},
    onTargetLanguageClick: () -> Unit = {},
    sourceText: String = "",
    targetText: String = "",
    onTTSClick: (String) -> Unit = {},
    onSourceTextClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val clipboardManger = LocalClipboardManager.current
    val interaction = remember {
        MutableInteractionSource()
    }
    val surfaceContainerHighestColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val surfaceColor = MaterialTheme.colorScheme.surface
    Surface {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = sourceLanguage,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = interaction, indication = null
                        ) {
                            onSourceLanguageClick()
                        })
                IconButton(onClick = {
                    clipboardManger.setText(AnnotatedString(sourceText))
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { onTTSClick(sourceText) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                    )
                }
            }
            Text(
                text = sourceText,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable(
                        interactionSource = interaction, indication = null
                    ) { onSourceTextClick(sourceText) },
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(200.dp)
                    .padding(20.dp), color = Color.DarkGray
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = targetLanguage,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = interaction, indication = null
                        ) {
                            onTargetLanguageClick()
                        })
                IconButton(onClick = {
                    clipboardManger.setText(AnnotatedString(targetText))
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
                IconButton(onClick = {
                    onTTSClick(targetText)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .padding(bottom = 10.dp)
            ) {
                if (targetText.isNotEmpty()) {
                    Text(
                        text = targetText,
                        color = MaterialTheme.colorScheme.surfaceTint,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .heightIn(
                                max = 300.dp
                            )
                            .verticalScroll(rememberScrollState())
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(30.dp)
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        Canvas(modifier = Modifier.matchParentSize()) {
                            val brush = Brush.horizontalGradient(
                                colors = listOf(
                                    surfaceContainerHighestColor.copy(0.6f), surfaceColor
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ContentAfterTranslatePreview() {
    EchoLinguaTheme {
        ContentAfterTranslate(
            sourceText = "早上好", targetText = "Good morning"
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ContentAfterTranslateDarkPreview() {
    EchoLinguaTheme {
        ContentAfterTranslate(
            sourceText = "早上好"
        )
    }
}