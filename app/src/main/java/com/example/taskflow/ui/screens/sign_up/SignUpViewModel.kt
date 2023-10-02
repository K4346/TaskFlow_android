package com.example.taskflow.ui.screens.sign_up

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.taskflow.AppNavigation
import com.example.taskflow.R
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountService
import com.example.taskflow.domain.repositories.LogService
import com.example.taskflow.domain.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService,
    private val application: Application
) : TaskFlowViewModel(logService, application) {

    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val authUseCase = AuthUseCase()

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        uiState.value.apply {

            if (!authUseCase.isValidEmail(email)) {
                Toast.makeText(
                    application.applicationContext,
                    R.string.email_error,
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            if (!authUseCase.isValidPassword(password)) {
                Toast.makeText(
                    application.applicationContext,
                    R.string.password_error,
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            if (!authUseCase.passwordMatches(password, repeatPassword)) {
                Toast.makeText(
                    application.applicationContext,
                    R.string.password_match_error,
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }
        launchCatching {
            accountService.createAccountWithEmailAndPassword(uiState.value.email,uiState.value.password)
            openAndPopUp(AppNavigation.MAIN_SCREEN,AppNavigation.SIGN_UP_SCREEN)
        }
    }

    fun onGoogleSignInCLick(idToken:String,openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.createAccountWithGoogle(idToken)
            openAndPopUp(AppNavigation.MAIN_SCREEN,AppNavigation.SIGN_UP_SCREEN)
        }
    }

}