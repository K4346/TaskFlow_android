package com.example.taskflow

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.repositories.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class TaskFlowViewModel(private val logService: LogService, val app: Application) :
    AndroidViewModel(app) {
    fun launchCatching(toast: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (toast) {
                    Log.e("kpop",throwable.message?:"Ошибка блин")
                    Toast.makeText(
                        app.applicationContext,
                        throwable.message?:"Ошибка блин",
                        Toast.LENGTH_LONG
                    ).show()
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}