package com.example.measurementbook.ui.singleui.verifyemailscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.measurementbook.MeasurementBookHiltApplication
import com.example.measurementbook.R
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val application: MeasurementBookHiltApplication,
    private val accountService: AccountService,
    private val logService: LogService
) : ViewModel() {

    val userEmail: String?
        get() =  accountService.userEmail

    var sendEmailVerificationError by mutableStateOf(false)
    private set

    fun onVerifyEmailClicked(
        navigateToSignIn: () -> Unit
    ) {
        if(accountService.isThereCurrentUser()) {
            accountService.sendVerificationEmail()
            navigateToSignIn()
        } else {
            logService.logKnownError(application.getString(R.string.email_verification_failed))
            sendEmailVerificationError = true
        }
    }

    fun onTryAgainClick() {
        sendEmailVerificationError = false
    }

}