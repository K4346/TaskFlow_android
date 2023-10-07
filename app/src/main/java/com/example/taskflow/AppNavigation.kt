package com.example.taskflow

import androidx.navigation.NavHostController


class AppNavigation(private val navController: NavHostController) {
    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String, inclusive_: Boolean = true) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = inclusive_ }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    companion object {
        const val SPLASH_SCREEN = "SplashScreen"
        const val MAIN_SCREEN = "MainScreen"
        const val LOGIN_SCREEN = "LoginScreen"
        const val SIGN_UP_SCREEN = "SignUpScreen"
        const val USER_INFO_SCREEN = "UserInfoScreen"
    }
}