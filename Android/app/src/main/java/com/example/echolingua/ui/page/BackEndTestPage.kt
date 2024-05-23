package com.example.echolingua.ui.page

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.nl.translate.TranslateLanguage

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BackEndTestPage(
    backEndTestPageViewModel: BackEndTestPageViewModel = viewModel()
) {
    val message by backEndTestPageViewModel.message.collectAsState()
    val internetPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.INTERNET,
            onPermissionResult = {
                backEndTestPageViewModel.setMessage("Permission granted")
            }
        )
    LaunchedEffect(Unit) {
        internetPermissionState.launchPermissionRequest()
    }
    Card(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                backEndTestPageViewModel.register("test", "test")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Register")
        }
        Button(
            onClick = {
                backEndTestPageViewModel.login("test", "test")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Login")
        }
        Button(
            onClick = {
                backEndTestPageViewModel.login("test", "test1")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Login error")
        }
        Button(
            onClick = {
                backEndTestPageViewModel.translate("test", "你好", "zh", "en")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Translate")
        }
        Button(
            onClick = {
                backEndTestPageViewModel.translate("test", "你好", "jp", TranslateLanguage.FRENCH)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Translate error")
        }
        Button(
            onClick = {
                backEndTestPageViewModel.getTTSService(
                    model = "Wanderer",
                    text = "把头低下！！！！",
                    language = "zh"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "TTS")
        }
        Button(
            onClick = {
                backEndTestPageViewModel.getTTSServiceByGet(
                    model = "Wanderer",
                    text = "就凭你也配直视我？",
                    language = "zh"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "TTS by get")
        }
        Text(text = message)
    }
}