package com.example.echolingua.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echolingua.ui.page.AudioTranscribePage
import com.example.echolingua.ui.page.BackEndTestPage
import com.example.echolingua.ui.page.CameraTranslatePage
import com.example.echolingua.ui.page.LanguageSelectStateHolder
import com.example.echolingua.ui.page.PagesAccessToLanguageSelectPage
import com.example.echolingua.ui.page.TranslatePage

@Composable
fun TranslatePagesNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteName.TRANSLATE_PAGE,
    onNavigateToBackEndTestPage: () -> Unit = {}
) {
    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(RouteName.TRANSLATE_PAGE) {
            TranslatePage(onNavigateToAudioTranscribePage = {
                navController.navigate(RouteName.AUDIO_TRANSCRIBE_PAGE)
            }, onNavigateToCameraTranslatePage = {
                navController.navigate(RouteName.CAMERA_TRANSLATE_PAGE)
            }, onNavigateToLanguageSelectPage = { selectMode ->
                LanguageSelectStateHolder.navigateToLanguageSelectPage(selectMode)
            }, onNavigateToBackEndTestPage = {
                onNavigateToBackEndTestPage()
            })
        }
        composable(RouteName.AUDIO_TRANSCRIBE_PAGE) {
            AudioTranscribePage()
        }
        composable(RouteName.CAMERA_TRANSLATE_PAGE) {
            CameraTranslatePage(onNavigateBackToTranslatePage = {
                navController.popBackStack()
            }, onNavigateToLanguageSelectPage = { selectMode ->
                LanguageSelectStateHolder.navigateToLanguageSelectPage(selectMode)
            })
        }
    }
}

@Composable
fun MainNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteName.TRANSLATE_PAGES
) {
    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(RouteName.TRANSLATE_PAGES) {
            PagesAccessToLanguageSelectPage(onNavigateToBackEndTestPage = {
                navController.navigate(RouteName.BACK_END_TEST_PAGE)
            })
        }
        composable(RouteName.BACK_END_TEST_PAGE) {
            BackEndTestPage()
        }

    }
}