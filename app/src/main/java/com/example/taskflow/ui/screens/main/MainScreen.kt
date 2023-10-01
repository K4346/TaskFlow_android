package com.example.taskflow.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskflow.AppNavigation
import com.example.taskflow.R
import com.example.taskflow.ui.composable.SettingsToolbar

@Composable
fun MainScreen(navigateAndPopUp: (String, String) -> Unit) {
    SettingsToolbar(title = "qwe", actionIcon =R.drawable.ic_settings) {
        navigateAndPopUp(
            AppNavigation.USER_INFO_SCREEN,
            AppNavigation.SIGN_UP_SCREEN
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen { k, v -> }
}
