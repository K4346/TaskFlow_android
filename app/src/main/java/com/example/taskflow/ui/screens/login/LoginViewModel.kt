package com.example.taskflow.ui.screens.login

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.taskflow.AppNavigation
import com.example.taskflow.AppNavigation.Companion.LOGIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.MAIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.SIGN_UP_SCREEN
import com.example.taskflow.R
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountRepository
import com.example.taskflow.domain.repositories.LogRepository
import com.example.taskflow.domain.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    logRepository: LogRepository,
    private val application: Application
) : TaskFlowViewModel(logRepository, application) {

    val uiState = mutableStateOf(LoginUiState())

    private val authUseCase = AuthUseCase()

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password


    fun onLoginClick(onLoginSuccess: (String, String) -> Unit) {
        if (!authUseCase.isValidEmail(email)) {
            Toast.makeText(
                application.applicationContext, R.string.email_error, Toast.LENGTH_LONG
            ).show()
            return
        }

        if (password.isBlank()) {
            Toast.makeText(
                application.applicationContext, R.string.password_error, Toast.LENGTH_LONG
            ).show()
            return
        }
        launchCatching {
            accountRepository.authenticate(email, password)
            onLoginSuccess(MAIN_SCREEN, LOGIN_SCREEN)
        }
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onForgotPasswordClick() {
        if (!authUseCase.isValidEmail(email)) {
            Toast.makeText(
                application.applicationContext, R.string.email_error, Toast.LENGTH_LONG
            ).show()
            return
        }

        launchCatching {
            accountRepository.sendRecoveryEmail(email)
            Toast.makeText(
                application.applicationContext,
                R.string.recovery_message_sent_to_email,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun onAnonymousClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountRepository.createAnonymousAccount()
            openAndPopUp(MAIN_SCREEN, AppNavigation.SPLASH_SCREEN)
        }
    }

    fun onRegisterAccountClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(SIGN_UP_SCREEN, LOGIN_SCREEN)
        }
    }

    //    todo объединить с регистрацией
    fun onGoogleSignInCLick(idToken: String, openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountRepository.createAccountWithGoogle(idToken)
            openAndPopUp(AppNavigation.MAIN_SCREEN, AppNavigation.LOGIN_SCREEN)
        }
    }

}