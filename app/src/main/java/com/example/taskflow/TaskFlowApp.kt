package com.example.taskflow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.AppNavigation.Companion.LOGIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.MAIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.SIGN_UP_SCREEN
import com.example.taskflow.AppNavigation.Companion.SPLASH_SCREEN
import com.example.taskflow.AppNavigation.Companion.USER_INFO_SCREEN
import com.example.taskflow.ui.screens.main.MainScreen
import com.example.taskflow.ui.screens.login.LoginScreen
import com.example.taskflow.ui.screens.sign_up.SignUpScreen
import com.example.taskflow.ui.screens.user_info.UserInfoScreen
import com.example.taskflow.ui.splash.SplashScreen

//todo руты можно вынести
@Composable
fun TaskFlowApp() {
    val navController = rememberNavController()
    val appNavigation = AppNavigation(navController)

    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN,
        //  enterTransition =   { slideInHorizontally(animationSpec = tween(700)) },
        // exitTransition =   { slideOutHorizontally(animationSpec = tween(700)) }
    ) {
        composable(SPLASH_SCREEN) {
            SplashScreen( { route, popUp -> appNavigation.navigateAndPopUp(route, popUp) })
        }
        composable(LOGIN_SCREEN) {
            LoginScreen( { route, popUp -> appNavigation.navigateAndPopUp(route, popUp) })
        }
        composable(SIGN_UP_SCREEN) {
            SignUpScreen( { route, popUp -> appNavigation.navigateAndPopUp(route, popUp) })
        }
        composable(USER_INFO_SCREEN) {
            UserInfoScreen()
        }
        composable(MAIN_SCREEN) {
            MainScreen { route, popUp -> appNavigation.navigate(route) }
        }
    }
}
