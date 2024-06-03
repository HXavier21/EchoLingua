package com.example.echolingua.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.echolingua.ui.navigator.TranslatePagesNavigator
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.ui.page.stateHolders.TranslateModelStateHolder

@Composable
fun PagesAccessToLanguageSelectPage(
    modifier: Modifier = Modifier,
    onNavigateToDataPage: () -> Unit,
    onSettingClick: () -> Unit = {}
) {
    val isSelecting by LanguageSelectStateHolder.isSelecting
    Box(modifier = modifier.fillMaxSize()) {
        TranslatePagesNavigator(
            onNavigateToDataPage = onNavigateToDataPage,
            onSettingClick = onSettingClick
        )
        AnimatedVisibility(
            visible = isSelecting,
            enter = fadeIn() + scaleIn(
                initialScale = 0.9f
            ),
            exit = fadeOut()
        ) {
            LanguageSelectPage(
                selectMode = LanguageSelectStateHolder.selectMode.value,
                onBackClick = { LanguageSelectStateHolder.isSelecting.value = false },
                onModelStateClick = {
                    TranslateModelStateHolder.currentLanguage.value = it
                    TranslateModelStateHolder.isManagingModel.value = true
                }
            )
        }
    }
}