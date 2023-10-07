package com.example.taskflow.domain.repositories

interface LogRepository {
    fun logNonFatalCrash(throwable: Throwable)
}
