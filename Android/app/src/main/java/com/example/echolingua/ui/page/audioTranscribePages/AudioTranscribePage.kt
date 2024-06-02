package com.example.echolingua.ui.page.audioTranscribePages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

data class Model(val name: String, val url: String, val fileSizeInMB: Int = 0)

val ModelList = listOf(
    Model(
        name = "ggml-tiny",
        url = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-tiny.bin",
        fileSizeInMB = 75
    ),
    Model(
        name = "ggml-base",
        url = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-base.bin",
        fileSizeInMB = 142
    ),
    Model(
        name = "ggml-small",
        url = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-small.bin",
        fileSizeInMB = 466
    ),
    Model(
        name = "ggml-distil-small",
        url = "https://huggingface.co/distil-whisper/distil-small.en/resolve/main/ggml-distil-small.en.bin",
        fileSizeInMB = 336
    ),
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioTranscribePage(
    audioTranscribePageViewModel: AudioTranscribePageViewModel = viewModel()
) {
    val text by audioTranscribePageViewModel.textFlow.collectAsState()
    val canTranscribe by audioTranscribePageViewModel.canTranscribeFlow.collectAsState()
    val modelState by audioTranscribePageViewModel.modelStateFlow.collectAsState()
    var inProcess by remember {
        mutableStateOf(false)
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    val audioRecordPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.RECORD_AUDIO,
        onPermissionResult = {
            if(it){
                inProcess = if (inProcess) {
                    audioTranscribePageViewModel.stopRecording()
                    false
                } else {
                    audioTranscribePageViewModel.startRecording()
                    true
                }
            }
        }
    )

    Card(modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = true
                },
                text = {
                    Text(
                        text = modelState.model?.name ?: "Select Model"
                    )
                },
                enabled = !inProcess
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ModelList.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            audioTranscribePageViewModel.viewModelScope.launch {
                                audioTranscribePageViewModel.selectModel(item)
                                audioTranscribePageViewModel.loadModelOrDownload()
                            }
                        },
                        text = {
                            Text(
                                text = item.name
                            )
                        }
                    )
                }
            }
        }
        LinearProgressIndicator(
            progress = {
                when (modelState.status) {
                    is AudioTranscribePageViewModel.ModelState.Status.Downloading -> {
                        (modelState.status as AudioTranscribePageViewModel.ModelState.Status.Downloading).progress
                    }

                    else -> 0f
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
                audioRecordPermissionState.launchPermissionRequest()
            },
            enabled = canTranscribe
        ) {
            Text(text = if (!inProcess) "record" else "stop")
        }
        Text(text = text)
    }
}