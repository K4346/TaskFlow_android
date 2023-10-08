package com.example.taskflow.ui.screens.user_info

data class UserInfoUiState(
    val name: String = "",
    val email: String = "",
    var photoUrl: String = "",
    val providerList: List<UserInfoViewModel.SignInProvider>
)
