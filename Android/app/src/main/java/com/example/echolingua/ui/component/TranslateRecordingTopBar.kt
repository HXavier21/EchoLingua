package com.example.echolingua.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.echolingua.ui.theme.EchoLinguaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateRecordingTopBar(
    onBackClick: () -> Unit = {}, onMoreClick: () -> Unit = {}, onBackClickEnabled: Boolean = true
) {
    TopAppBar(title = { }, navigationIcon = {
        IconButton(
            onClick = { onBackClick() }, enabled = onBackClickEnabled
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back"
            )
        }
    }, actions = {
        IconButton(onClick = { onMoreClick() }) {
            Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = "More")
        }
    })
}

@Composable
@Preview
fun TranslateRecordingTopBarPreview() {
    EchoLinguaTheme {
        TranslateRecordingTopBar()
    }
}