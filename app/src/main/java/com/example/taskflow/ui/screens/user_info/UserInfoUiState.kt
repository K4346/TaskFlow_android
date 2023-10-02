package com.example.taskflow.ui.screens.user_info

import android.net.Uri

data class UserInfoUiState(
    val name:String?,
    val email:String?,
    val photoUrl: Uri?,
    val uid:String
)
