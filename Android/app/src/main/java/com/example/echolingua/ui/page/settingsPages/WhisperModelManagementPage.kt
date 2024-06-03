package com.example.echolingua.ui.page.settingsPages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.echolingua.ui.component.myDetectTapGestures
import com.example.echolingua.ui.page.audioTranscribePages.ModelList
import com.example.echolingua.ui.page.stateHolders.WhisperModelStateHolder
import com.example.echolingua.ui.theme.EchoLinguaTheme
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhisperModelManagementPage(
    onNavigateBackToSettingsPage: () -> Unit = {}
) {
    val modelStateMap = WhisperModelStateHolder.modelStateMap
    val progress by WhisperModelStateHolder.downloadProgress
    val coroutine = rememberCoroutineScope()
    BackHandler {
        onNavigateBackToSettingsPage()
    }
    Scaffold(topBar = {
        LargeTopAppBar(title = {
            Text(
                text = "Whisper Models", style = MaterialTheme.typography.headlineSmall
            )
        }, navigationIcon = {
            IconButton(onClick = { onNavigateBackToSettingsPage() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back"
                )
            }
        }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert, contentDescription = "More"
                )
            }
        })
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            for (model in ModelList) {
                item {
                    Row(modifier = Modifier
                        .clickable { }
                        .pointerInput(Unit) {
                            myDetectTapGestures(onLongPress = {
                                coroutine.launch {
                                    withContext(Dispatchers.IO) {
                                        WhisperModelStateHolder.deleteModel(model)
                                    }
                                }
                            })
                        }
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = model.name + " (${model.fileSizeInMB} MB)",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        when (modelStateMap[model]) {
                            WhisperModelStateHolder.ModelStatus.Downloading -> {
                                CircularProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .padding(end = 6.dp)
                                        .size(24.dp)
                                )
                            }

                            WhisperModelStateHolder.ModelStatus.Downloaded -> {
                                RadioButton(selected = false, onClick = {
                                    coroutine.launch {
                                        withContext(Dispatchers.IO) {
                                            WhisperModelStateHolder.loadModel(model)
                                        }
                                    }
                                })
                            }

                            WhisperModelStateHolder.ModelStatus.Loaded -> {
                                RadioButton(selected = true, onClick = {}, enabled = false)

                            }

                            WhisperModelStateHolder.ModelStatus.NotDownloaded -> {
                                IconButton(onClick = {
                                    coroutine.launch {
                                        withContext(Dispatchers.IO) {
                                            WhisperModelStateHolder.downloadModel(model)
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.FileDownload,
                                        contentDescription = "Download",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            null -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun WhisperModelManagementPagePreview() {
    EchoLinguaTheme {
        WhisperModelManagementPage()
    }
}