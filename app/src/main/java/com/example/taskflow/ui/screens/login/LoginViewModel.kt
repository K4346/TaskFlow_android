package com.example.taskflow.ui.screens.login

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.example.taskflow.AppNavigation.Companion.LOGIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.MAIN_SCREEN
import com.example.taskflow.R
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountService
import com.example.taskflow.domain.repositories.LogService
import com.example.taskflow.domain.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService, logService: LogService, private val application: Application
) : TaskFlowViewModel(logService, application) {

    val uiState = mutableStateOf(LoginUiState())

    private val authUseCase = AuthUseCase()

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password


    fun onLoginClick(onLoginSuccess: (String, String) -> Unit) {
        if (!authUseCase.isValidEmail(email)) {
            Toast.makeText(
                application.applicationContext,
                R.string.email_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (password.isBlank()) {
            Toast.makeText(
                application.applicationContext,
                R.string.password_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }
        launchCatching {
            accountService.authenticate(email, password)
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
                application.applicationContext,
                R.string.email_error,
                Toast.LENGTH_LONG
            ).show()
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            Toast.makeText(
                application.applicationContext,
                R.string.recovery_message_sent_to_email,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}