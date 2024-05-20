package com.example.echolingua.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.echolingua.ui.navigator.TranslatePagesNavigator

@Composable
fun PagesAccessToLanguageSelectPage(
    modifier: Modifier = Modifier
) {
    val isSelecting by LanguageSelectStateHolder.isSelecting
    Box(modifier = modifier.fillMaxSize()) {
        TranslatePagesNavigator()
        AnimatedVisibility(
            visible = isSelecting,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            LanguageSelectPage(
                onBackClick = { LanguageSelectStateHolder.isSelecting.value = false }
            )
        }
    }
}