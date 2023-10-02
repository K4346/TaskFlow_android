package com.example.taskflow.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.AppNavigation
import com.example.taskflow.R
import com.example.taskflow.ui.composable.SettingsToolbar

@Composable
fun MainScreen(
    navigate: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    SettingsToolbar(title = "qwe", actionIcon = R.drawable.ic_settings) {
        navigate(
            AppNavigation.USER_INFO_SCREEN,
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen({q->})
}
