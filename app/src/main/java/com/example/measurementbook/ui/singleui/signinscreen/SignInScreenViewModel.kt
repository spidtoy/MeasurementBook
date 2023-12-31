package com.example.measurementbook.ui.singleui.signinscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.measurementbook.generalutilities.uiutilities.isEmailValid
import com.example.measurementbook.network.services.AccountService
import com.example.measurementbook.network.services.LogService
import com.example.measurementbook.network.services.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
    ) : ViewModel() {

    var emailAddress by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var signInState: MutableStateFlow<SignInState> = accountService.signInState
        private set

    var showErrorLoginInputs by mutableStateOf(false)
        private set

    fun resetSignInState() {
        accountService.resetSignInState()
    }

    fun updateEmailAddress(newEmailAddress: String) {
        if(showErrorLoginInputs){showErrorLoginInputs = false}
        emailAddress = newEmailAddress
    }

    fun updatePassword(newPassword: String) {
        if(showErrorLoginInputs){showErrorLoginInputs = false}
        password = newPassword
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    private val signInErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
        accountService.signInState.value = SignInState.Error(throwable)
    }

    fun signIn() {
        if(!(emailAddress.isEmailValid()) || password.length < 6 ) {
            showErrorLoginInputs = true
        } else {
            showErrorLoginInputs = false
            viewModelScope.launch(signInErrorExceptionHandler) {
                accountService.signIn(emailAddress, password)
                if(accountService.isEmailVerified!!){
                    accountService.signInState.value = SignInState.SignInSuccessfulToList
                }else{
                    accountService.signInState.value = SignInState.SignInSuccessfulToVerify
                }
            }
        }
    }
}