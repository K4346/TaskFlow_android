/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.makeitso.screens.splash

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.example.taskflow.AppNavigation.Companion.MAIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.SIGN_UP_SCREEN
import com.example.taskflow.AppNavigation.Companion.SPLASH_SCREEN
import com.example.taskflow.AppNavigation.Companion.USER_INFO_SCREEN
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountService
import com.example.taskflow.domain.repositories.LogService
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
 //todo потом написать configurationService: ConfigurationService,
  private val accountService: AccountService,
  logService: LogService, app: Application
) : TaskFlowViewModel(logService, app) {
  val showError = mutableStateOf(false)

// todo init {
//    launchCatching { configurationService.fetchConfiguration() }
//  }

  fun onAppStart(openAndPopUp: (String, String) -> Unit) {
    showError.value = false
    if (accountService.hasUser) openAndPopUp(SIGN_UP_SCREEN, SPLASH_SCREEN)
    else createAnonymousAccount(openAndPopUp)
  }

  private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
    launchCatching(toast = false) {
      try {
        accountService.createAnonymousAccount()
      } catch (ex: FirebaseAuthException) {
        showError.value = true
        throw ex
      }
      openAndPopUp(SIGN_UP_SCREEN, SPLASH_SCREEN)
    }
  }
}
