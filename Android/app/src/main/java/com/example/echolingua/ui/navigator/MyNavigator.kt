package com.example.echolingua.ui.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echolingua.ui.page.AudioTranscribePage

@Composable
fun MyNavigator(
    navController: NavHostController = rememberNavController(),
    startDestination: String = RouteName.AUDIO_TRANSCRIBE_PAGE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(RouteName.AUDIO_TRANSCRIBE_PAGE) {
            AudioTranscribePage()
        }
    }
}