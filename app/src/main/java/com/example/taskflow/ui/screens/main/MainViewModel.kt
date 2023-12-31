package com.example.taskflow.ui.screens.main

import android.app.Application
import com.example.taskflow.TaskFlowViewModel
import com.example.taskflow.domain.repositories.AccountRepository
import com.example.taskflow.domain.repositories.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class MainViewModel(
    application: Application
) : TaskFlowViewModel(application) {

}