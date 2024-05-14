package com.example.echolingua.ui.page

import android.Manifest
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolingua.MainActivity
import com.example.echolingua.R
import com.example.echolingua.ui.component.PhotoPreview
import com.example.echolingua.util.TextRecognizer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.nl.translate.TranslateLanguage
import java.io.File
import kotlin.math.log

private const val TAG = "CameraTranslatePage"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraTranslatePage(
    cameraTranslatePageViewModel: CameraTranslatePageViewModel = viewModel(),
    onNavigateBackToTranslatePage: () -> Unit = {}
) {
    val context = LocalContext.current
    val imageFile by cameraTranslatePageViewModel.imageFileFlow.collectAsState()
    val recognizedText by cameraTranslatePageViewModel.recognizedTextFlow.collectAsState()
    var isCaptured by remember { mutableStateOf(false) }
    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }

    val cameraPermissionState =
        rememberPermissionState(
            permission = Manifest.permission.CAMERA,
            onPermissionResult = {
                if (it) {
                    (context as MainActivity).startCamera(width, height)
                }
            }
        )
    val mediaPermissionState =
        rememberPermissionState(
            permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else Manifest.permission.READ_EXTERNAL_STORAGE,
            onPermissionResult = {
                (context as MainActivity).pickMedia(
                    onSuccess = { uri ->
                        Log.d(TAG, "CameraTranslatePage: $uri")
                        context.contentResolver
                            .openInputStream(uri)
                            ?.use {
                                File
                                    .createTempFile("temp", ".jpeg")
                                    .apply {
                                        writeBytes(it.readBytes())
                                    }
                            }
                            ?.let {
                                cameraTranslatePageViewModel.setImageFile(it)
                            }
                        TextRecognizer.processImage(
                            imageFile = uri,
                            language = TranslateLanguage.CHINESE,
                            refreshRecognizedText = {
                                cameraTranslatePageViewModel.setRecognizedText(
                                    it
                                )
                            }
                        )
                        isCaptured = true
                        context.stopCamera()
                    },
                    onFailure = {
                        Log.d(TAG, "CameraTranslatePage: No image picked")
                        Toast
                            .makeText(
                                context,
                                "No image picked",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                )
            }
        )

    BackHandler {
        if (isCaptured) {
            isCaptured = false
            cameraTranslatePageViewModel.setRecognizedText(
                com.google.mlkit.vision.text.Text("", listOf<String>())
            )
            cameraPermissionState.launchPermissionRequest()
        } else {
            (context as MainActivity).stopCamera()
            onNavigateBackToTranslatePage()
        }
    }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                width = it.size.width
                height = it.size.height
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AndroidView(
                factory = {
                    LayoutInflater
                        .from(it)
                        .inflate(R.layout.camera_view, null, false)
                },
                modifier = Modifier.fillMaxSize()
            )
            when (isCaptured) {
                false -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = "Pick image",
                                modifier = Modifier
                                    .clickable {
                                        mediaPermissionState.launchPermissionRequest()
                                    }
                                    .background(
                                        Color.Black.copy(alpha = 0.8f),
                                        CircleShape
                                    )
                                    .padding(15.dp)
                            )
                        }

                        LargeFloatingActionButton(
                            onClick = {
                                (context as MainActivity).takePhoto(
                                    onImageSavedCallback = { file ->
                                        cameraTranslatePageViewModel.setImageFile(file)
                                        TextRecognizer.processImage(
                                            imageFile = file.toUri(),
                                            language = TranslateLanguage.CHINESE,
                                            refreshRecognizedText = {
                                                cameraTranslatePageViewModel.setRecognizedText(it)
                                            }
                                        )
                                        isCaptured = true
                                        context.stopCamera()
                                    }
                                )
                            },
                            shape = CircleShape,
                            modifier = Modifier.size(90.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Camera,
                                contentDescription = "Take a photo",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                true -> {
                    PhotoPreview(
                        imageFile = imageFile,
                        recognizeText = recognizedText
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(text = recognizedText.text)
                    }
                }
            }
        }
    }
}