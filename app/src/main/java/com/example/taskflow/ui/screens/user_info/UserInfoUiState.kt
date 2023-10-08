package com.example.taskflow.ui.screens.user_info

data class UserInfoUiState(
    val name: String = "",
    val email: String = "",
    var photoUrl: String = "",
    val providerList: List<SignInProvider>
)
enum class SignInProvider(val providerName: String = "") {
    Google("Google-Auth"), Email("Email-Auth"), Anonymous("Anonymous-Auth")
}
