package com.example.measurementbook.network.impl

import com.example.measurementbook.network.services.LogService
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LogServiceImpl @Inject constructor(): LogService {
    override fun logNonFatalCrash(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
    }

    override fun logKnownError(message: String) {
        Firebase.crashlytics.log(message)
    }
}