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

package com.example.taskflow.ui.splash

import android.app.Application
import com.example.taskflow.AppNavigation.Companion.LOGIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.MAIN_SCREEN
import com.example.taskflow.AppNavigation.Companion.SPLASH_SCREEN
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountRepository
import com.example.taskflow.domain.repositories.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    //todo потом написать configurationService: ConfigurationService,
    private val accountRepository: AccountRepository, logRepository: LogRepository, app: Application
) : TaskFlowViewModel(logRepository, app) {

// todo init {
//    launchCatching { configurationService.fetchConfiguration() }
//  }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountRepository.hasUser) openAndPopUp(MAIN_SCREEN, SPLASH_SCREEN)
        else openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
    }
}
