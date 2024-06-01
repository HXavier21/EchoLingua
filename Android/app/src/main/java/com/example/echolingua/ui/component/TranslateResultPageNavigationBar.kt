package com.example.echolingua.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateResultPageNavigationBar(
    onBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onStarClick: () -> Unit = {}
) {
    TopAppBar(title = {}, navigationIcon = {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
            )
        }
    }, actions = {
        IconButton(onClick = { onHistoryClick() }) {
            Icon(
                imageVector = Icons.Outlined.History, contentDescription = "History"
            )
        }
        IconButton(onClick = { onStarClick() }) {
            Icon(
                Icons.Filled.Star, contentDescription = "Star"
            )
        }
        IconButton(onClick = { onMoreClick() }) {
            Icon(
                Icons.Filled.MoreVert, contentDescription = "Feedback"
            )
        }
    })
}
