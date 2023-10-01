package com.example.taskflow.domain.repositories

import com.example.taskflow.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface AccountService {
//todo переделать
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<UserEntity>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun createAccountWithEmailAndPassword(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
//    todo репозиторий
    fun getUserInfo()
}