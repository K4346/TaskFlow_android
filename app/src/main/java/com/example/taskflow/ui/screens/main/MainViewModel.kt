package com.example.taskflow.ui.screens.main

import android.app.Application
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountService
import com.example.taskflow.domain.repositories.LogService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val accountService: AccountService, logService: LogService, application: Application
) : TaskFlowViewModel(logService, application) {

}