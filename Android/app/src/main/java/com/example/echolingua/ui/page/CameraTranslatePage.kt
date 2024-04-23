package com.example.echolingua.ui.page

import android.view.LayoutInflater
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.echolingua.MainActivity
import com.example.echolingua.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraTranslatePage(
    cameraTranslatePageViewModel: CameraTranslatePageViewModel = viewModel()
) {
    val context = LocalContext.current

    val cameraPermissionState =
        rememberPermissionState(
            permission = android.Manifest.permission.CAMERA,
            onPermissionResult = {
                if (it) {
                    (context as MainActivity).startCamera()
                }
            }
        )

    LaunchedEffect(key1 = Unit) {
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
            Button(
                onClick = {
                    (context as MainActivity).takePhoto()
                },
                shape = ButtonDefaults.outlinedShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                Text(text = "Take photo")
            }
        }

    }
}