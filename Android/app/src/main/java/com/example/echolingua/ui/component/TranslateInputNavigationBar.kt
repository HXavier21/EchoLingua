package com.example.echolingua.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateInputNavigationBar(onBackClick: () -> Unit) {
    TopAppBar(title = {}, navigationIcon = {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }, actions = {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Outlined.Draw,
                contentDescription = "round_trip"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "history Icon"
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "feedback",
            )
        }
    })
}