package com.example.taskflow.ui.screens.user_info

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.taskflow.AppNavigation
import com.example.taskflow.R
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.entities.UserEntity
import com.example.taskflow.domain.repositories.AccountService
import com.example.taskflow.domain.repositories.LogService
import com.example.taskflow.domain.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService,
    private val application: Application
) : TaskFlowViewModel(logService, application) {

    val uiState = mutableStateOf(accountService.currentUser?.let {
        UserEntity(
            id = it.uid,
            isAnonymous = it.isAnonymous,
            name = it.displayName,
            email = it.email,
            photoUrl = it.photoUrl,
            providerData = it.providerData
        )
    } ?: UserEntity())

    private val email
        get() = uiState.value.email
    private val name
        get() = uiState.value.name

    private val authUseCase = AuthUseCase()

    var photoUrl = mutableStateOf(
        if (uiState.value.photoUrl == null) "" else
            uiState.value.photoUrl.toString()
    )

    enum class SignInProvider(val providerName: String = "") {
        Google("Google-Auth"), Email("Email-Auth"), Anonymous("Anonymous-Auth")
    }

    //   todo interactor (этот метод выглядит не оч, но к сожалению отображть анонимность вместе с другими текущими способами авторизацию некорректно)
    fun getProviders(): List<SignInProvider> {
        val listProviders = mutableListOf<SignInProvider>()
        uiState.value.providerData?.forEach {
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
            accountService.signOut()
            openAndPopUp(AppNavigation.LOGIN_SCREEN, AppNavigation.USER_INFO_SCREEN)
        }
    }

    fun deleteAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
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
        photoUrl.value = newValue
    }

    fun changeProfile(openAndPopUp: (String, String) -> Unit) {

        if (email != null && !authUseCase.isValidEmail(email!!)) {
            Toast.makeText(
                application.applicationContext,
                R.string.email_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (name!=null && name!!.isBlank()) {
            Toast.makeText(
                application.applicationContext,
                R.string.name_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (photoUrl.value.isNotEmpty() && !authUseCase.isValidPhotoUrl(photoUrl.value)) {
            Toast.makeText(
                application.applicationContext,
                R.string.image_url_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        launchCatching {
            accountService.updateProfile(userEntity = uiState.value)
            Toast.makeText(
                application.applicationContext,
                R.string.profile_change_data_success,
                Toast.LENGTH_LONG
            ).show()
            openAndPopUp(AppNavigation.MAIN_SCREEN, AppNavigation.USER_INFO_SCREEN)
        }
    }

}