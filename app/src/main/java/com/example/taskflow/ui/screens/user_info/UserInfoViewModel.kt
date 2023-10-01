package com.example.taskflow.ui.screens.user_info

import android.app.Application
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountService
import com.example.taskflow.domain.repositories.LogService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserInfoViewModel @Inject constructor(private val accountService: AccountService, logService: LogService, application: Application
) : TaskFlowViewModel(logService, application) {
    val uiState = accountService.currentUser.map { it.isAnonymous }
}