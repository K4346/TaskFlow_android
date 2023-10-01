package com.example.taskflow.domain.repositories

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
