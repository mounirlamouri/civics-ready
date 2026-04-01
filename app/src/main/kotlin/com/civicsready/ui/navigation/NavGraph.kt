package com.civicsready.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.civicsready.ui.home.HomeScreen
import com.civicsready.ui.practice.PracticeScreen
import com.civicsready.ui.settings.SettingsScreen
import com.civicsready.ui.test.TestResultScreen
import com.civicsready.ui.test.TestScreen

object Routes {
    const val HOME     = "home"
    const val PRACTICE = "practice"
    const val TEST     = "test"
    const val RESULT   = "result"
    const val SETTINGS = "settings"
}

@Composable
fun CivicsNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onPractice = { navController.navigate(Routes.PRACTICE) },
                onTest     = { navController.navigate(Routes.TEST) },
                onSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.PRACTICE) {
            PracticeScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.TEST) {
            TestScreen(
                onBack   = { navController.popBackStack() },
                onResult = { navController.navigate(Routes.RESULT) }
            )
        }

        composable(Routes.RESULT) {
            val testEntry = remember(navController) {
                navController.getBackStackEntry(Routes.TEST)
            }
            TestResultScreen(
                onHome    = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = false } } },
                onRetry   = { navController.navigate(Routes.TEST) { popUpTo(Routes.RESULT) { inclusive = true } } },
                viewModel = hiltViewModel(testEntry)
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
