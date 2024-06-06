package com.example.echolingua.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.example.echolingua.util.TextProcessUtil

@Composable
fun AudioTranslateResult(
    modifier: Modifier = Modifier,
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    sourceText: String = "",
    targetText: String = "",
    onTTSClick: (String) -> Unit = {},
    onDetailsClick: () -> Unit = {},
    updateStyleCallback: (TextStyle) -> Unit = {}
) {
    var height by remember { mutableIntStateOf(0) }
    var width by remember { mutableIntStateOf(0) }
    var style by remember { mutableStateOf(TextStyle()) }
    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)
        .onGloballyPositioned {
            height = it.size.height
        }) {
        Text(
            text = "$sourceLanguage -> $targetLanguage",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Gray
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (height / 3).pixelToDp())
                .verticalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val myId = "inlineContent"
            Text(text = buildAnnotatedString {
                append(sourceText)
                appendInlineContent(myId, "[icon]")
            }, modifier = Modifier
                .weight(1f)
                .onGloballyPositioned {
                    width = it.size.width
                }, style = TextProcessUtil.calTextStyle(
                text = sourceText, maxWidthInPx = width
            ) {
                style = it
                updateStyleCallback(it)
            }, inlineContent = mapOf(
                Pair(myId, InlineTextContent(
                    Placeholder(
                        width = style.fontSize,
                        height = style.fontSize,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    IconButton(onClick = { onTTSClick(sourceText) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                            contentDescription = "TTS"
                        )
                    }
                })
            )
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 40.dp, end = 60.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            IconButton(
                onClick = { onDetailsClick() }, colors = IconButtonDefaults.iconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = "Details"
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (height / 2).pixelToDp())
                .verticalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val myId = "inlineContent"
            Text(
                text = buildAnnotatedString {
                    append(targetText)
                    appendInlineContent(myId, "[icon]")
                },
                modifier = Modifier.weight(1f),
                style = style,
                color = MaterialTheme.colorScheme.primary,
                inlineContent = mapOf(
                    Pair(myId, InlineTextContent(
                        Placeholder(
                            width = style.fontSize,
                            height = style.fontSize,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                        )
                    ) {
                        IconButton(onClick = { onTTSClick(targetText) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                contentDescription = "TTS",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    })
                )
            )
        }
    }
}

@Preview
@Composable
fun AudioTranslateResultPreview() {
    EchoLinguaTheme {
        Surface {
            AudioTranslateResult(
                sourceText = "hellosahwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww",
                targetText = "hellosahwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"
            )
        }
    }
}