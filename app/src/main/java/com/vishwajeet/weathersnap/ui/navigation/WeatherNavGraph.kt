package com.vishwajeet.weathersnap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.vishwajeet.weathersnap.ui.camera.CameraScreen
import com.vishwajeet.weathersnap.ui.report.CreateReportScreen
import com.vishwajeet.weathersnap.ui.report.CreateReportViewModel
import com.vishwajeet.weathersnap.ui.saved.SavedReportsScreen
import com.vishwajeet.weathersnap.ui.weather.WeatherScreen

@Composable
fun WeatherNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.WeatherRoute
    ) {

        composable<Screen.WeatherRoute> {
            WeatherScreen(
                onNavigateToCreateReport = { route ->
                    navController.navigate(route)
                },
                onNavigateToReports = {
                    navController.navigate(Screen.SavedReportsRoute)
                },

                )
        }

        composable<Screen.CreateReportRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.CreateReportRoute>()

            CreateReportScreen(
                weatherData = args,
                onNavigateToCamera = {
                    navController.navigate(Screen.CameraRoute) {
                        launchSingleTop = true
                    }
                },
                onSaveSuccess = {
                    navController.navigate(Screen.SavedReportsRoute) {
                        popUpTo<Screen.CreateReportRoute> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screen.SavedReportsRoute> {
            SavedReportsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable<Screen.CameraRoute> { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<Screen.CreateReportRoute>()
            }

            val reportViewModel: CreateReportViewModel =
                hiltViewModel(parentEntry)

            CameraScreen(
                onImageCaptured = { uri ->
                    reportViewModel.processCapturedImage(
                        navController.context,
                        uri
                    )
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}