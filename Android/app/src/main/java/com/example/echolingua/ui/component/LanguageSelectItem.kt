package com.example.echolingua.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.page.TranslateModelState

@Composable
fun LanguageSelectItem(
    key: String,
    name: String,
    selectedLanguage: String,
    translateModelState: TranslateModelState,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {},
    onModelStateClick: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                if (selectedLanguage == key) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedLanguage == key) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.padding(start = 15.dp)
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 15.dp
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        when (translateModelState) {
            TranslateModelState.DOWNLOADED -> {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(end = 10.dp)
                        .clickable {
                           onModelStateClick(key)
                        }
                )
            }

            TranslateModelState.NOT_DOWNLOADED -> {
                Icon(
                    imageVector = Icons.Outlined.FileDownload,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(end = 10.dp)
                        .clickable {
                            onModelStateClick(key)
                        }
                )
            }

            TranslateModelState.DOWNLOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(end = 10.dp)
                        .size(24.dp)
                )
            }
        }
    }
}
