package com.example.measurementbook.ui.singleui.changepasswordscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.ChangePasswordState
import com.example.measurementbook.network.services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
) : ViewModel() {
    var oldPassword by mutableStateOf("")
        private set

    var newPassword by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var changePasswordState: MutableStateFlow<ChangePasswordState> = accountService.changePasswordState
        private set

    var lowOldPasswordStrength by mutableStateOf(false)
        private set

    var lowNewPasswordStrength by mutableStateOf(false)
        private set

    var showPassword by mutableStateOf(false)
        private set

    var passwordNotMatch by mutableStateOf(false)
        private set

    fun togglePassword() {
        showPassword = !showPassword
    }

    fun onOldPasswordChange(newText: String) {
        if(lowOldPasswordStrength){lowOldPasswordStrength = false}
        oldPassword = newText
    }

    fun onNewPasswordChange(newText: String) {
        if(lowNewPasswordStrength){lowNewPasswordStrength = false}
        if(passwordNotMatch){passwordNotMatch = false}
        newPassword = newText
    }

    fun onConfirmPasswordValueChange(newText: String){
        if(passwordNotMatch){passwordNotMatch = false}
        confirmPassword = newText
    }

    private val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        accountService.changePasswordState.value = ChangePasswordState.Error(throwable)
        logService.logNonFatalCrash(throwable)
    }

    fun onChangePasswordButtonClicked() {
        if(oldPassword.length < 6) {
            lowOldPasswordStrength = true
        } else if(newPassword.length < 6) {
            lowNewPasswordStrength = true
        } else if (newPassword != confirmPassword){
            passwordNotMatch = true
        } else {
            viewModelScope.launch(showErrorExceptionHandler) {
                accountService.reAuthenticate(oldPassword)
                accountService.updatePassword(newPassword)
                accountService.changePasswordState.value = ChangePasswordState.ChangePasswordSuccess
            }
        }
    }

    fun onTryAgainClick(){
        accountService.resetChangePasswordState()
    }
}