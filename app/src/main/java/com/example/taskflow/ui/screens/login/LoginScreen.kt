package com.example.taskflow.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.R
import com.example.taskflow.ui.composable.*

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    BasicToolbar(stringResource(id = R.string.login_title))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, viewModel::onEmailChange,
//          todo  Modifier.fieldModifier()
        )
        PasswordField(uiState.password, viewModel::onPasswordChange,
        //todo    Modifier.fieldModifier()
        )

        BasicButton(
            R.string.sign_in,
           //todo Modifier.basicButton()
        ) { viewModel.onLoginClick(openAndPopUp) }

        BasicTextButton(R.string.forgot_password,
            //todo Modifier.textButton()
        ) {
            viewModel.onForgotPasswordClick()
        }
    }
}