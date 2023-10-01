

package com.example.taskflow.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskflow.R

import com.example.makeitso.screens.splash.SplashViewModel
import com.example.taskflow.ui.composable.BasicButton
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
  openAndPopUp: (String, String) -> Unit,
  viewModel: SplashViewModel = hiltViewModel()
) {
  Column(
    modifier =
    Modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .background(color = MaterialTheme.colors.background)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (viewModel.showError.value) {
      Text(text = stringResource(id = R.string.whats_wrong))

      BasicButton(R.string.try_again) { viewModel.onAppStart(openAndPopUp) }
    } else {
      CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
    }
  }

  LaunchedEffect(true) {
    delay(SPLASH_TIMEOUT)
    viewModel.onAppStart(openAndPopUp)
  }
}
