package com.example.taskflow.domain.repositories

import com.example.taskflow.data.entities.UserEntity
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val currentUserId: String
    val hasUser: Boolean
    val currentUser: FirebaseUser?

    val currentUserEntity: Flow<UserEntity>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun createAccountWithEmailAndPassword(email: String, password: String)

    suspend fun createAccountWithGoogle(tokenId: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()

    //    todo репозиторий
    fun getUserInfo(): UserEntity

    suspend fun updateProfile(userEntity: UserEntity)
}