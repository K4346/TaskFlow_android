package com.example.taskflow.ui.screens.user_info

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.taskflow.AppNavigation
import com.example.taskflow.R
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.data.entities.UserEntity
import com.example.taskflow.domain.use_cases.AuthUseCase
import com.example.taskflow.domain.use_cases.UserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userInfoUseCase: UserInfoUseCase,
    application: Application
) : TaskFlowViewModel(application) {

    val uiState = mutableStateOf(initUiState())

    private val _snackbarMessage = mutableStateOf<String?>(null)
    val snackbarMessage: State<String?>
        get() = _snackbarMessage

    private val email
        get() = uiState.value.email
    private val name
        get() = uiState.value.name
    private val photoUrl
        get() = uiState.value.photoUrl

    private val authUseCase = AuthUseCase()

    private fun initUiState(): UserInfoUiState {
        val userInfo = userInfoUseCase.getUserInfo()
        val state = with(userInfo) {
            UserInfoUiState(
                name ?: "", email ?: "", photoUrl?.toString() ?: "",
                userInfoUseCase.getProviders(providerData, app.applicationContext)
            )
        }
        return state
    }

    private fun prepareUserEntityFromUserInfoUiState(): UserEntity {
        val userInfoBase = userInfoUseCase.getUserInfo()

        return UserEntity(
            userInfoBase.id,
            userInfoBase.isAnonymous,
            name,
            email,
            if (photoUrl.isBlank()) null else Uri.parse(photoUrl),
            userInfoBase.providerData
        )
    }

    fun signOut(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            userInfoUseCase.signOut()
            openAndPopUp(AppNavigation.LOGIN_SCREEN, AppNavigation.USER_INFO_SCREEN)
        }
    }

    fun deleteAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            userInfoUseCase.deleteAccount()
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


    fun isAccountAnonymous() = userInfoUseCase.isAccountAnonymous()


    fun changeProfile(openAndPopUp: (String, String) -> Unit) {
        val userData = userInfoUseCase.getUserInfo()
        if (userInfoUseCase.nothingChanged(userData, name, email, photoUrl)) {
            showSnackbar(app.getString(R.string.nothing_to_change))
            return
        }

        if (email != userData.email && !authUseCase.isValidEmail(email)) {
            Toast.makeText(
                app.applicationContext,
                R.string.email_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (name != userData.name && !authUseCase.isValidAccountName(name)) {
            Toast.makeText(
                app.applicationContext,
                R.string.name_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (photoUrl != (userData.photoUrl?.toString() ?: "")
            && !authUseCase.isValidPhotoUrl(photoUrl)
        ) {
            Toast.makeText(
                app.applicationContext,
                R.string.image_url_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        launchCatching {
            userInfoUseCase.updateProfile(userEntity = prepareUserEntityFromUserInfoUiState())
            Toast.makeText(
                app.applicationContext,
                R.string.profile_change_data_success,
                Toast.LENGTH_LONG
            ).show()
            openAndPopUp(AppNavigation.MAIN_SCREEN, AppNavigation.USER_INFO_SCREEN)
        }
    }

    private fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

}