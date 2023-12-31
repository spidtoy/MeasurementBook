package com.example.measurementbook.ui.singleui.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.generalutilities.uiutilities.isEmailValid
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.ResetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
) : ViewModel() {
    var email by mutableStateOf("")
        private set

    var emailError by mutableStateOf(false)
        private set

    var resetPasswordState: MutableStateFlow<ResetPasswordState> = accountService.resetPasswordState
        private set

    fun onEmailValueChange(newText: String) {
        if(emailError){emailError = false}
        email = newText
    }

    private val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        accountService.resetPasswordState.value = ResetPasswordState.Error(throwable)
        logService.logNonFatalCrash(throwable)
    }

    fun onResetPasswordButtonClicked() {
        if(!email.isEmailValid()) {
            emailError = true
        } else {
            viewModelScope.launch(showErrorExceptionHandler) {
                accountService.sendPasswordResetEmail(email)
                accountService.resetPasswordState.value = ResetPasswordState.Success
            }
        }
    }

    fun onTryAgainClick() {
        accountService.resetResetPasswordState()
    }
}