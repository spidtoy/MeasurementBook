package com.example.measurementbook.ui.singleui.signupscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.generalutilities.uiutilities.isEmailValid
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
    ) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var emailAddress by mutableStateOf("")
        private set

    var password by mutableStateOf("")
    private set

    var signUpState: MutableStateFlow<SignUpState> = accountService.signUpState
    private set

    var confirmPassword by mutableStateOf("")
    private set

    var showPassword by mutableStateOf(false)
    private set

    var nameError by mutableStateOf(false)
        private set

    var emailError by mutableStateOf(false)
        private set

    var lowPasswordStrength by mutableStateOf(false)
        private set

    var passwordNotMatch by mutableStateOf(false)
        private set

    fun resetSignUpState() {
        accountService.resetSignUpState()
    }

    fun onNameValueChange(newName: String) {
            if(nameError) { nameError = false }
            name = newName
        }

    fun onEmailValueChange(newEmail: String) {
        if(emailError) { emailError = false }
        emailAddress = newEmail
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    private val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        accountService.signUpState.value = SignUpState.Error(throwable)
        logService.logNonFatalCrash(throwable)
    }

    fun onContinueButtonClicked() {
        if (name == "") {
            nameError = true
        } else if(!emailAddress.isEmailValid()) {
            emailError = true
        } else if(password.length < 6){
            lowPasswordStrength = true
        } else if (password != confirmPassword) {
            passwordNotMatch = true
        } else {
            if (lowPasswordStrength) {
                lowPasswordStrength = false
            }
            if (passwordNotMatch) {
                passwordNotMatch = false
            }
            viewModelScope.launch(showErrorExceptionHandler) {
                accountService.createUser(emailAddress, password)
                accountService.setDisplayName(name)
            }
        }
    }
}