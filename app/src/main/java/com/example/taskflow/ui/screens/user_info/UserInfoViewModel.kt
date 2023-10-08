package com.example.taskflow.ui.screens.user_info

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.taskflow.AppNavigation
import com.example.taskflow.R
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.data.entities.UserEntity
import com.example.taskflow.domain.repositories.AccountRepository
import com.example.taskflow.domain.repositories.LogRepository
import com.example.taskflow.domain.use_cases.AuthUseCase
import com.google.firebase.auth.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    logRepository: LogRepository,
    private val application: Application
) : TaskFlowViewModel(logRepository, application) {

    val uiState = mutableStateOf(initUiState())

    private val email
        get() = uiState.value.email
    private val name
        get() = uiState.value.name
    private val photoUrl
        get() = uiState.value.photoUrl

    private val authUseCase = AuthUseCase()

    private fun initUiState(): UserInfoUiState {
        val userInfo = accountRepository.getUserInfo()
        val state = with(userInfo) {
            UserInfoUiState(
                name ?: "", email ?: "", photoUrl?.toString() ?: "",
                getProviders(providerData)
            )
        }
        return state
    }

    private fun prepareUserEntityFromUserInfoUiState(): UserEntity {
        val userInfoBase = accountRepository.getUserInfo()

        return UserEntity(
            userInfoBase.id,
            userInfoBase.isAnonymous,
            name,
            email,
            if (photoUrl.isBlank()) null else Uri.parse(photoUrl),
            userInfoBase.providerData
        )
    }


    enum class SignInProvider(val providerName: String = "") {
        Google("Google-Auth"), Email("Email-Auth"), Anonymous("Anonymous-Auth")
    }

    //   todo interactor (этот метод выглядит не оч, но к сожалению отображть анонимность вместе с другими текущими способами авторизацию некорректно)
    private fun getProviders(providerData: List<UserInfo>?): List<SignInProvider> {
        val listProviders = mutableListOf<SignInProvider>()
        providerData?.forEach {
            if (it.providerId == application.getString(R.string.google_com)) {
                listProviders.add(SignInProvider.Google)
            } else if (it.providerId == application.getString(R.string.passwordEng)) {
                listProviders.add(SignInProvider.Email)
            }
        }
        if (listProviders.isEmpty()) listProviders.add(SignInProvider.Anonymous)
        return listProviders
    }

    fun signOut(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountRepository.signOut()
            openAndPopUp(AppNavigation.LOGIN_SCREEN, AppNavigation.USER_INFO_SCREEN)
        }
    }

    fun deleteAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountRepository.deleteAccount()
            openAndPopUp(AppNavigation.LOGIN_SCREEN, AppNavigation.USER_INFO_SCREEN)
        }
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onPhotoUrlChange(newValue: String) {
        uiState.value = uiState.value.copy(photoUrl = newValue)
    }

    //todo перенести часть логики в useCase (а желательно все что касается репозитория)
    fun isAccountAnonymous() = accountRepository.getUserInfo().isAnonymous

    private fun nothingChanged(userData: UserEntity) =
        (
                email == userData.email
                        && name == userData.name
                        && photoUrl == (userData.photoUrl?.toString() ?: "")
                )


    fun changeProfile(openAndPopUp: (String, String) -> Unit) {
        val userData = accountRepository.getUserInfo()
        if (nothingChanged(userData)) {
            Toast.makeText(
                application.applicationContext,
                R.string.nothing_to_change,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (email != userData.email && !authUseCase.isValidEmail(email)) {
            Toast.makeText(
                application.applicationContext,
                R.string.email_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (name != userData.name && !authUseCase.isValidAccountName(name)) {
            Toast.makeText(
                application.applicationContext,
                R.string.name_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (photoUrl != (userData.photoUrl?.toString() ?: "")
            && !authUseCase.isValidPhotoUrl(photoUrl)
        ) {
            Toast.makeText(
                application.applicationContext,
                R.string.image_url_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        launchCatching {
            accountRepository.updateProfile(userEntity = prepareUserEntityFromUserInfoUiState())
            Toast.makeText(
                application.applicationContext,
                R.string.profile_change_data_success,
                Toast.LENGTH_LONG
            ).show()
            openAndPopUp(AppNavigation.MAIN_SCREEN, AppNavigation.USER_INFO_SCREEN)
        }
    }

}