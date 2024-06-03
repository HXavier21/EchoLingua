package com.example.echolingua.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echolingua.ui.page.BackEndTestPage
import com.example.echolingua.ui.page.DataPage
import com.example.echolingua.ui.page.PagesAccessToLanguageSelectPage
import com.example.echolingua.ui.page.audioTranscribePages.AudioTranscribePage
import com.example.echolingua.ui.page.cameraTranscribePages.CameraTranslatePage
import com.example.echolingua.ui.page.mainTranslatePages.MainTranslatePage
import com.example.echolingua.ui.page.settingsPages.SettingsPage
import com.example.echolingua.ui.page.settingsPages.WhisperModelManagementPage
import com.example.echolingua.ui.page.stateHolders.LanguageSelectStateHolder

@Composable
fun TranslatePagesNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteName.MAIN_TRANSLATE_PAGE,
    onNavigateToDataPage: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(RouteName.MAIN_TRANSLATE_PAGE) {
            MainTranslatePage(onNavigateToCameraPage = {
                navController.navigate(RouteName.CAMERA_TRANSLATE_PAGE)
            }, onLanguageSelectClick = {
                LanguageSelectStateHolder.navigateToLanguageSelectPage(it)
            }, onNavigateToDataPage = {
                onNavigateToDataPage()
            }, onSettingClick = {
                onSettingClick()
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
            PagesAccessToLanguageSelectPage(onNavigateToDataPage = {
                navController.navigate(RouteName.DATA_PAGE)
            }, onSettingClick = {
                navController.navigate(RouteName.SETTINGS_PAGE)
            })
        }
        composable(RouteName.BACK_END_TEST_PAGE) {
            BackEndTestPage()
        }
        composable(RouteName.DATA_PAGE) {
            DataPage(onNavigateBackToMainTranslatePage = {
                navController.popBackStack()
            })
        }
        composable(RouteName.SETTINGS_PAGE) {
            SettingsPage(onNavigateBackToMainPage = {
                navController.popBackStack()
            }, onNavigateToWhisperModelsPage = {
                navController.navigate(RouteName.WHISPER_MODELS_PAGE)
            })
        }
        composable(RouteName.WHISPER_MODELS_PAGE) {
            WhisperModelManagementPage(onNavigateBackToSettingsPage = {
                navController.popBackStack()
            })
        }
    }
}