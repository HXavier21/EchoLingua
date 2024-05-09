package com.example.echolingua.ui.page

import android.view.LayoutInflater
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.echolingua.MainActivity
import com.example.echolingua.R
import com.example.echolingua.ui.component.PhotoPreview
import com.example.echolingua.util.TextRecognizer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.nl.translate.TranslateLanguage

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraTranslatePage(
    cameraTranslatePageViewModel: CameraTranslatePageViewModel = viewModel(),
    onNavigateBackToTranslatePage: () -> Unit = {}
) {
    val context = LocalContext.current
    val imageFile by cameraTranslatePageViewModel.imageFileFlow.collectAsState()
    var isCaptured by remember { mutableStateOf(false) }
    val displayMetrics = context.resources.displayMetrics
    val width = displayMetrics.widthPixels - 50
    val height = displayMetrics.heightPixels

    val cameraPermissionState =
        rememberPermissionState(
            permission = android.Manifest.permission.CAMERA,
            onPermissionResult = {
                if (it) {
                    (context as MainActivity).startCamera(width, height)
                }
            }
        )

    BackHandler {
        if (isCaptured) {
            isCaptured = false
            cameraPermissionState.launchPermissionRequest()
        } else {
            (context as MainActivity).stopCamera()
            onNavigateBackToTranslatePage()
        }
    }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Card(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AndroidView(
                factory = {
                    LayoutInflater
                        .from(it)
                        .inflate(R.layout.camera_view, null)
                },
                modifier = Modifier.fillMaxSize()
            )
            when (isCaptured) {
                false -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                (context as MainActivity).takePhoto(
                                    onImageSavedCallback = {
                                        cameraTranslatePageViewModel.setImageFile(it)
                                        TextRecognizer.processImage(
                                            it.toUri(),
                                            TranslateLanguage.ENGLISH
                                        )
                                        isCaptured = true
                                        context.stopCamera()
                                    }
                                )
                            },
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Default.Camera,
                                contentDescription = "Take a photo"
                            )
                        }
                    }
                }

                true -> {
                    PhotoPreview(
                        painter = rememberAsyncImagePainter(imageFile)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(text = "Transcribed text here")
                    }
                }
            }
        }
    }
}