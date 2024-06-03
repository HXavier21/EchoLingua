package com.example.echolingua.ui.page.settingsPages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    onNavigateToWhisperModelsPage: () -> Unit = {},
    onNavigateBackToMainPage: () -> Unit = {}
) {
    BackHandler {
        onNavigateBackToMainPage()
    }
    Surface {
        Column {
            LargeTopAppBar(title = {
                Text(
                    text = "Settings", style = MaterialTheme.typography.headlineSmall
                )
            }, navigationIcon = {
                IconButton(onClick = {onNavigateBackToMainPage()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }, actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert, contentDescription = "More"
                    )
                }
            })
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(text = "Whisper Model Management",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToWhisperModelsPage() }
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge)
                }
                item {
                    Text(text = "TTS Model Management",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge)
                }

                item {
                    Text(text = "OCR Model Management",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsPage() {
    SettingsPage()
}