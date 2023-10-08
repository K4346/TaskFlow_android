package com.example.taskflow.domain.use_cases

import android.content.Context
import com.example.taskflow.R
import com.example.taskflow.data.entities.UserEntity
import com.example.taskflow.domain.repositories.AccountRepository
import com.example.taskflow.ui.screens.user_info.SignInProvider
import com.google.firebase.auth.UserInfo
import javax.inject.Inject

interface UserInfoUseCase {
    suspend fun signOut()
    fun getUserInfo(): UserEntity
    suspend fun updateProfile(userEntity: UserEntity)
    fun isAccountAnonymous(): Boolean
    suspend fun deleteAccount()
    fun nothingChanged(userData: UserEntity, name: String, email: String, photoUrl: String): Boolean
    fun getProviders(providerData: List<UserInfo>?, context: Context): List<SignInProvider>
}

class UserInfoUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository
) : UserInfoUseCase {

    override fun getUserInfo() = accountRepository.getUserInfo()

    override suspend fun signOut() = accountRepository.signOut()
    override suspend fun deleteAccount() = accountRepository.deleteAccount()
    override suspend fun updateProfile(userEntity: UserEntity) =
        accountRepository.updateProfile(userEntity)

    override fun isAccountAnonymous() = getUserInfo().isAnonymous

    override fun nothingChanged(
        userData: UserEntity,
        name: String,
        email: String,
        photoUrl: String
    ) =
        (
                email == userData.email
                        && name == userData.name
                        && photoUrl == (userData.photoUrl?.toString() ?: "")
                )

    //   этот метод выглядит не оч, но к сожалению отображть анонимность вместе с другими текущими способами авторизацию некорректно

    override fun getProviders(
        providerData: List<UserInfo>?,
        context: Context
    ): List<SignInProvider> {
        val listProviders = mutableListOf<SignInProvider>()
        providerData?.forEach {
            if (it.providerId == context.getString(R.string.google_com)) {
                listProviders.add(SignInProvider.Google)
            } else if (it.providerId == context.getString(R.string.passwordEng)) {
                listProviders.add(SignInProvider.Email)
            }
        }
        if (listProviders.isEmpty()) listProviders.add(SignInProvider.Anonymous)
        return listProviders
    }

}