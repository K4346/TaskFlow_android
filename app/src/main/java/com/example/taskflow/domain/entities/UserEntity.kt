package com.example.taskflow.domain.entities

import android.net.Uri
import com.google.firebase.auth.UserInfo

data class UserEntity(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val name: String? = null,
    val email: String? = null,
    val photoUrl: Uri? = null,
    val providerData: List<UserInfo>? = null
)

