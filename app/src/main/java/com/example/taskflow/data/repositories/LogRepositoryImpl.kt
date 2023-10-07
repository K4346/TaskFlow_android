package com.example.taskflow.data.repositories

import com.example.taskflow.domain.repositories.LogRepository
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor() : LogRepository {
    override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
}
