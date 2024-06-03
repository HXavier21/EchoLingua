package com.example.echolingua.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MainPageLanguageSelector(
    modifier: Modifier = Modifier,
    sourceLanguage: String = "Source",
    targetLanguage: String = "Target",
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    textPadding: Dp = 10.dp,
    onSourceLanguageClick: () -> Unit = {},
    onTargetLanguageClick: () -> Unit = {},
    onSwapLanguageClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Button(
            onClick = { onSourceLanguageClick() },
            shape = shape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = sourceLanguage,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = textPadding),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (sourceLanguage == "Auto detect") {
            IconButton(onClick = {}, enabled = false) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null
                )
            }
        } else {
            IconButton(onClick = { onSwapLanguageClick() }) {
                Icon(
                    Icons.Outlined.SwapHoriz, "swap language", modifier = Modifier.scale(1.2f)
                )
            }
        }
        Button(
            onClick = { onTargetLanguageClick() },
            shape = shape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = targetLanguage,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = textPadding),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun MainPageLanguageSelectorPreview() {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        MainPageLanguageSelector(sourceLanguage = "English",
            targetLanguage = "Chinese",
            onSourceLanguageClick = {},
            onTargetLanguageClick = {},
            onSwapLanguageClick = {})
    }
}