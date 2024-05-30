package com.example.echolingua.ui.navigator

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.echolingua.ui.navigator.RouteName.SELECT_MODE
import com.example.echolingua.ui.navigator.RouteName.withArgument
import com.example.echolingua.ui.navigator.RouteName.withArgumentKey
import com.example.echolingua.ui.page.AudioTranscribePage
import com.example.echolingua.ui.page.CameraTranslatePage
import com.example.echolingua.ui.page.DataPage
import com.example.echolingua.ui.page.LanguageSelectPage
import com.example.echolingua.ui.page.MainTranslatePage
import com.example.echolingua.ui.page.SelectMode
import com.example.echolingua.ui.page.TranslatePage

@Composable
fun MyNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteName.MAIN_TRANSLATE_PAGE
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
                navController.navigate(RouteName.LANGUAGE_SELECT_PAGE withArgument selectMode)
            })
        }
        composable(RouteName.AUDIO_TRANSCRIBE_PAGE) {
            AudioTranscribePage()
        }
        composable(RouteName.CAMERA_TRANSLATE_PAGE) {
            CameraTranslatePage(onNavigateBackToTranslatePage = {
                navController.popBackStack()
            }, onNavigateToSourceLanguageSelectPage = { selectMode ->
                navController.navigate(
                    RouteName.LANGUAGE_SELECT_PAGE withArgument selectMode,
                )
            }, onNavigateToTargetLanguageSelectPage = { selectMode ->
                navController.navigate(
                    RouteName.LANGUAGE_SELECT_PAGE withArgument selectMode,
                )
            })
        }
        composable(
            RouteName.LANGUAGE_SELECT_PAGE withArgumentKey SELECT_MODE,
            arguments = listOf(navArgument(SELECT_MODE) {
                type = NavType.EnumType(SelectMode::class.java)
            })
        ) {
            LanguageSelectPage(selectMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) it.arguments?.getSerializable(
                SELECT_MODE, SelectMode::class.java
            ) as SelectMode
            else it.arguments?.getSerializable(SELECT_MODE) as SelectMode, onBackClick = {
                navController.popBackStack()
            })
        }

        composable(RouteName.MAIN_TRANSLATE_PAGE) {
            MainTranslatePage(onNavigateToDataPage = {
                navController.navigate(RouteName.DATA_PAGE)
            })
        }

        composable(RouteName.DATA_PAGE) {
            DataPage(onNavigateBackToMainTranslatePage={
                navController.popBackStack()
            })
        }
    }
}