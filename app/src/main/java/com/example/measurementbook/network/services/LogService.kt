package com.example.measurementbook.network.services

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
    fun logKnownError(message: String)
}