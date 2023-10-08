package com.example.taskflow

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.domain.repositories.LogRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


open class TaskFlowViewModel(val app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var logRepository: LogRepository
    fun launchCatching(toast: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (toast) {
                    Log.e("kpop", throwable.message ?: "Ошибка блин")
                    Toast.makeText(
                        app.applicationContext,
                        throwable.message ?: "Ошибка блин",
                        Toast.LENGTH_LONG
                    ).show()
                }
                logRepository.logNonFatalCrash(throwable)
            }, block = block
        )
}