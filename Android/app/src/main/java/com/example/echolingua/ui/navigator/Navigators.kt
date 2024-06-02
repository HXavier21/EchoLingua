package com.example.echolingua.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echolingua.ui.page.audioTranscribePages.AudioTranscribePage
import com.example.echolingua.ui.page.BackEndTestPage
import com.example.echolingua.ui.page.DataPage
import com.example.echolingua.ui.page.cameraTranscribePages.CameraTranslatePage
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder
import com.example.echolingua.ui.page.mainTranslatePages.MainTranslatePage
import com.example.echolingua.ui.page.PagesAccessToLanguageSelectPage

@Composable
fun TranslatePagesNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteName.TRANSLATE_PAGE,
    onNavigateToBackEndTestPage: () -> Unit = {},
    onNavigateToDataPage: () -> Unit = {}
) {
    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(RouteName.TRANSLATE_PAGE) {
            MainTranslatePage(
                onNavigateToAudioTranscribePage = {
                    navController.navigate(RouteName.AUDIO_TRANSCRIBE_PAGE)
                },
                onNavigateToCameraPage = {
                    navController.navigate(RouteName.CAMERA_TRANSLATE_PAGE)
                },
                onLanguageSelectClick = {
                    LanguageSelectStateHolder.navigateToLanguageSelectPage(it)
                },
                onNavigateToDataPage = {
                    onNavigateToDataPage()
                }
            )
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
            }, onNavigateToDataPage = {
                navController.navigate(RouteName.DATA_PAGE)
            })
        }
        composable(RouteName.BACK_END_TEST_PAGE) {
            BackEndTestPage()
        }
        composable(RouteName.DATA_PAGE) {
            DataPage(
                onNavigateBackToMainTranslatePage = {
                    navController.popBackStack()
                }
            )
        }
    }
}