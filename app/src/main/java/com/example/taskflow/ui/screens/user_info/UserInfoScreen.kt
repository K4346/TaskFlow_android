package com.example.taskflow.ui.screens.user_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.taskflow.R
import com.example.taskflow.ui.composable.BasicButton
import com.example.taskflow.ui.composable.BasicField
import com.example.taskflow.ui.composable.EmailField

@Composable
fun UserInfoScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: UserInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val photoUrl by viewModel.photoUrl
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = uiState.photoUrl,
            contentDescription = null,

            )

        if (!uiState.isAnonymous) {
            uiState.email?.let { EmailField(it, viewModel::onEmailChange) }
            uiState.name?.let {
                BasicField(
                    hint = stringResource(R.string.name),
                    value = it,
                    onNewValue = viewModel::onNameChange
                )
            }
            BasicField(
                hint = stringResource(R.string.image_url),
                photoUrl,
                viewModel::onPhotoUrlChange
            )
            BasicButton(R.string.change_data, action = { viewModel.changeProfile(openAndPopUp) })
        }

        viewModel.getProviders().forEach {
            Text(text = it.providerName)
        }


        if (!uiState.isAnonymous) {
            BasicButton(R.string.log_out, action = { viewModel.signOut(openAndPopUp) })
        }

        BasicButton(R.string.delete_account, action = { viewModel.deleteAccount(openAndPopUp) })

    }

}


